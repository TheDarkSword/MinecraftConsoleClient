package it.revarmygaming.minecraftconsoleclient;

import com.github.steveice10.mc.auth.data.GameProfile;
import com.github.steveice10.mc.auth.exception.request.RequestException;
import com.github.steveice10.mc.auth.service.AuthenticationService;
import com.github.steveice10.mc.auth.service.SessionService;
import com.github.steveice10.mc.protocol.MinecraftConstants;
import com.github.steveice10.mc.protocol.MinecraftProtocol;
import com.github.steveice10.mc.protocol.ServerLoginHandler;
import com.github.steveice10.mc.protocol.data.SubProtocol;
import com.github.steveice10.mc.protocol.data.game.entity.player.GameMode;
import com.github.steveice10.mc.protocol.data.message.Message;
import com.github.steveice10.mc.protocol.data.message.MessageSerializer;
import com.github.steveice10.mc.protocol.data.status.PlayerInfo;
import com.github.steveice10.mc.protocol.data.status.ServerStatusInfo;
import com.github.steveice10.mc.protocol.data.status.VersionInfo;
import com.github.steveice10.mc.protocol.data.status.handler.ServerInfoBuilder;
import com.github.steveice10.mc.protocol.data.status.handler.ServerInfoHandler;
import com.github.steveice10.mc.protocol.data.status.handler.ServerPingTimeHandler;
import com.github.steveice10.mc.protocol.packet.ingame.client.ClientChatPacket;
import com.github.steveice10.mc.protocol.packet.ingame.client.player.ClientPlayerPositionPacket;
import com.github.steveice10.mc.protocol.packet.ingame.client.world.ClientTeleportConfirmPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.ServerChatPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.ServerJoinGamePacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.ServerTabCompletePacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.entity.ServerEntityPositionPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.entity.ServerEntityPositionRotationPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.entity.ServerEntityTeleportPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.entity.ServerEntityVelocityPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.entity.player.ServerPlayerPositionRotationPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.entity.spawn.ServerSpawnEntityPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.world.ServerSpawnPositionPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.world.ServerUpdateTimePacket;
import com.github.steveice10.opennbt.tag.builtin.*;
import com.github.steveice10.packetlib.Client;
import com.github.steveice10.packetlib.ProxyInfo;
import com.github.steveice10.packetlib.Server;
import com.github.steveice10.packetlib.Session;
import com.github.steveice10.packetlib.event.server.ServerAdapter;
import com.github.steveice10.packetlib.event.server.ServerClosedEvent;
import com.github.steveice10.packetlib.event.server.SessionAddedEvent;
import com.github.steveice10.packetlib.event.server.SessionRemovedEvent;
import com.github.steveice10.packetlib.event.session.*;
import com.github.steveice10.packetlib.tcp.TcpSessionFactory;
import com.google.gson.JsonParser;
import it.revarmygaming.minecraftconsoleclient.entity.Player;
import it.revarmygaming.minecraftconsoleclient.logger.Logger;
import it.revarmygaming.minecraftconsoleclient.message.MinecraftMessage;
import it.revarmygaming.minecraftconsoleclient.parser.ChatMessage;
import it.revarmygaming.minecraftconsoleclient.parser.Console;
import javafx.application.Platform;
import javafx.scene.control.TextField;

import java.net.Proxy;
import java.util.Arrays;
import java.util.Map;

public class Minecraft {

    private final String USERNAME;
    private final String PASSWORD;
    private final boolean VERIFY_USER;

    private Player player;
    private Logger logger;
    private TextField input;
    private Session session;

    private static final ProxyInfo PROXY = null;
    private static final Proxy AUTH_PROXY = Proxy.NO_PROXY;

    public Minecraft(String username, String password, boolean verify_user){
        this.USERNAME = username;
        this.PASSWORD = password;
        this.VERIFY_USER = verify_user;
    }

    public Minecraft(String username, String password){
        this(username, password, true);
    }

    public Minecraft(String username){
        this(username, null, false);
    }

    public void startServer(String host, int port, boolean onlineMode, int maxPlayers){
        SessionService sessionService = new SessionService();
        sessionService.setProxy(AUTH_PROXY);

        Server server = new Server(host, port, MinecraftProtocol.class, new TcpSessionFactory());
        server.setGlobalFlag(MinecraftConstants.SESSION_SERVICE_KEY, sessionService);
        server.setGlobalFlag(MinecraftConstants.VERIFY_USERS_KEY, onlineMode);
        server.setGlobalFlag(MinecraftConstants.SERVER_INFO_BUILDER_KEY, (ServerInfoBuilder) session -> new ServerStatusInfo(
                new VersionInfo(MinecraftConstants.GAME_VERSION, MinecraftConstants.PROTOCOL_VERSION),
                new PlayerInfo(maxPlayers, 0, new GameProfile[0]),
                MessageSerializer.fromString("Hello world!"),
                null
        ));

        server.setGlobalFlag(MinecraftConstants.SERVER_LOGIN_HANDLER_KEY, (ServerLoginHandler) session -> session.send(new ServerJoinGamePacket(
                0,
                false,
                GameMode.SURVIVAL,
                GameMode.SURVIVAL,
                1,
                new String[] {"minecraft:world"},
                getDimensionTag(),
                getOverworldTag(),
                "minecraft:world",
                100,
                0,
                16,
                false,
                false,
                false,
                false
        )));

        server.setGlobalFlag(MinecraftConstants.SERVER_COMPRESSION_THRESHOLD, 100);
        server.addListener(new ServerAdapter() {
            @Override
            public void serverClosed(ServerClosedEvent event) {
                System.out.println("Server closed.");
            }

            @Override
            public void sessionAdded(SessionAddedEvent event) {
                event.getSession().addListener(new SessionAdapter() {
                    @Override
                    public void packetReceived(PacketReceivedEvent event) {
                        /*if(event.getPacket() instanceof ClientChatPacket) {
                            ClientChatPacket packet = event.getPacket();
                            GameProfile profile = event.getSession().getFlag(MinecraftConstants.PROFILE_KEY);
                            System.out.println(profile.getName() + ": " + packet.getMessage());
                            String msg = "Hello, " + profile.getName() + "!";

                            event.getSession().send(new ServerChatPacket(msg));
                        }*/
                    }
                });
            }

            @Override
            public void sessionRemoved(SessionRemovedEvent event) {
                MinecraftProtocol protocol = (MinecraftProtocol) event.getSession().getPacketProtocol();
                if(protocol.getSubProtocol() == SubProtocol.GAME) {
                    System.out.println("Closing server.");
                    event.getServer().close(false);
                }
            }
        });

        server.bind();
    }

    public void ping(String host, int port) {
        SessionService sessionService = new SessionService();
        sessionService.setProxy(AUTH_PROXY);

        MinecraftProtocol protocol = new MinecraftProtocol(SubProtocol.STATUS);
        Client client = new Client(host, port, protocol, new TcpSessionFactory(PROXY));
        client.getSession().setFlag(MinecraftConstants.SESSION_SERVICE_KEY, sessionService);
        client.getSession().setFlag(MinecraftConstants.SERVER_INFO_HANDLER_KEY, (ServerInfoHandler) (session, info) -> {
            System.out.println("Version: " + info.getVersionInfo().getVersionName() + ", " + info.getVersionInfo().getProtocolVersion());
            System.out.println("Player Count: " + info.getPlayerInfo().getOnlinePlayers() + " / " + info.getPlayerInfo().getMaxPlayers());
            System.out.println("Players: " + Arrays.toString(info.getPlayerInfo().getPlayers()));
            System.out.println("Description: " + info.getDescription());
            //System.out.println("Icon: " + Arrays.toString(info.getIconPng()));
        });

        client.getSession().setFlag(MinecraftConstants.SERVER_PING_TIME_HANDLER_KEY, (ServerPingTimeHandler) (session, pingTime) ->
                System.out.println("Server ping took " + pingTime + "ms"));

        client.getSession().connect();
        while(client.getSession().isConnected()) {
            try {
                Thread.sleep(5);
            } catch(InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void login(String host, int port, Logger logger, TextField input){
        this.logger = logger;
        this.input = input;
        MinecraftProtocol protocol;
        if(VERIFY_USER) {
            try {
                AuthenticationService authService = new AuthenticationService();
                authService.setUsername(USERNAME);
                authService.setPassword(PASSWORD);
                authService.setProxy(AUTH_PROXY);
                authService.login();

                // Can also use "new MinecraftProtocol(USERNAME, PASSWORD)"
                // if you don't need a proxy or any other customizations.
                protocol = new MinecraftProtocol(authService);
                System.out.println("Successfully authenticated user.");
            } catch(RequestException e) {
                e.printStackTrace();
                return;
            }
        } else {
            protocol = new MinecraftProtocol(USERNAME);
        }



        SessionService sessionService = new SessionService();
        sessionService.setProxy(AUTH_PROXY);

        Client client = new Client(host, port, protocol, new TcpSessionFactory(PROXY));
        client.getSession().setFlag(MinecraftConstants.SESSION_SERVICE_KEY, sessionService);
        session = client.getSession();
        client.getSession().addListener(new SessionAdapter() {
            @Override
            public void packetReceived(PacketReceivedEvent event) {
                /*if(!(event.getPacket() instanceof ServerUpdateTimePacket)) {
                    System.out.println(event.getPacket().getClass().getSimpleName());
                }*/
                /*if(event.getPacket() instanceof ServerEntityPositionPacket){
                    ServerEntityPositionPacket packet = event.getPacket();
                    System.out.println("P.Id:" + packet.getEntityId() + " - My: " + player.getEntityId());
                } else if(event.getPacket() instanceof ServerEntityPositionRotationPacket){
                    ServerEntityPositionRotationPacket packet = event.getPacket();
                    System.out.println("PR.Id:" + packet.getEntityId() + " - My: " + player.getEntityId());
                }*/
                //System.out.println(event.getPacket().getClass().getSimpleName());
                if(event.getPacket() instanceof ServerJoinGamePacket) {
                    player = new Player(event.<ServerJoinGamePacket>getPacket().getEntityId());
                    if(input != null) Platform.runLater(() -> input.setDisable(false));
                    //event.getSession().send(new ClientChatPacket("Hello, this is a test of MCProtocolLib."));
                } else if(event.getPacket() instanceof ServerChatPacket) {
                    Message message = event.<ServerChatPacket>getPacket().getMessage();
                    /*ChatMessage chatMessage = new ChatMessage(message.toString());
                    Console.println(chatMessage);*/
                    logger.write(message.toString());
                    //event.getSession().disconnect("Finished");
                } else if(event.getPacket() instanceof ServerPlayerPositionRotationPacket) {
                    ServerPlayerPositionRotationPacket packet = event.getPacket();
                    /*System.out.println("ServerPlayerPositionRotation-X: " + packet.getX());
                    System.out.println("ServerPlayerPositionRotation-Y: " + packet.getY());
                    System.out.println("ServerPlayerPositionRotation-Z: " + packet.getZ());*/
                    /*System.out.println("x:" + packet.getX() +
                            ",y:" + packet.getY() +
                            ",z:" + packet.getZ() +
                            ",yaw:" + packet.getYaw() +
                            ",pitch:" + packet.getPitch());*/
                    player.setLocation(packet.getX(), packet.getY(), packet.getZ(), packet.getYaw(), packet.getPitch());
                    session.send(new ClientTeleportConfirmPacket(packet.getTeleportId()));
                } else if(event.getPacket() instanceof ServerTabCompletePacket){
                    ServerTabCompletePacket packet = event.getPacket();

                    System.out.println(Arrays.toString(packet.getMatches()));
                }
            }

            @Override
            public void packetSending(PacketSendingEvent event) {
                //System.out.println(event.getPacket().getClass().getSimpleName());
            }

            @Override
            public void packetSent(PacketSentEvent event) {
                //System.out.println(event.getPacket().getClass().getSimpleName());
            }

            @Override
            public void disconnected(DisconnectedEvent event) {
                System.out.println("Disconnected: " + event.getReason());
                String reason = JsonParser.parseString(event.getReason()).getAsJsonObject().get("with").getAsString();
                logger.write(new MinecraftMessage.Builder()
                        .setText(reason)
                        .setColor("red").build());
                if(input != null) Platform.runLater(() -> input.setDisable(true));
                if(event.getCause() != null) {
                    event.getCause().printStackTrace();
                }

                logger.write(new MinecraftMessage.Builder()
                        .setText("Disconnected from server. Reopen the client to enter again.")
                        .setColor("red").build());
            }
        });

        client.getSession().connect(true);
    }

    public void sendChatMessage(String message){
        if(session != null)
            session.send(new ClientChatPacket(message));
    }

    public Player getPlayer() {
        return player;
    }

    public Logger getLogger() {
        return logger;
    }

    public Session getSession(){
        return session;
    }

    private static CompoundTag getDimensionTag() {
        CompoundTag tag = new CompoundTag("");

        CompoundTag dimensionTypes = new CompoundTag("minecraft:dimension_type");
        dimensionTypes.put(new StringTag("type", "minecraft:dimension_type"));
        ListTag dimensionTag = new ListTag("value");
        CompoundTag overworldTag = convertToValue("minecraft:overworld", 0, getOverworldTag().getValue());
        dimensionTag.add(overworldTag);
        dimensionTypes.put(dimensionTag);
        tag.put(dimensionTypes);

        CompoundTag biomeTypes = new CompoundTag("minecraft:worldgen/biome");
        biomeTypes.put(new StringTag("type", "minecraft:worldgen/biome"));
        ListTag biomeTag = new ListTag("value");
        CompoundTag plainsTag = convertToValue("minecraft:plains", 0, getPlainsTag().getValue());
        biomeTag.add(plainsTag);
        biomeTypes.put(biomeTag);
        tag.put(biomeTypes);

        return tag;
    }

    private static CompoundTag getOverworldTag() {
        CompoundTag overworldTag = new CompoundTag("");
        overworldTag.put(new StringTag("name", "minecraft:overworld"));
        overworldTag.put(new ByteTag("piglin_safe", (byte) 0));
        overworldTag.put(new ByteTag("natural", (byte) 1));
        overworldTag.put(new FloatTag("ambient_light", 0f));
        overworldTag.put(new StringTag("infiniburn", "minecraft:infiniburn_overworld"));
        overworldTag.put(new ByteTag("respawn_anchor_works", (byte) 0));
        overworldTag.put(new ByteTag("has_skylight", (byte) 1));
        overworldTag.put(new ByteTag("bed_works", (byte) 1));
        overworldTag.put(new StringTag("effects", "minecraft:overworld"));
        overworldTag.put(new ByteTag("has_raids", (byte) 1));
        overworldTag.put(new IntTag("logical_height", 256));
        overworldTag.put(new FloatTag("coordinate_scale", 1f));
        overworldTag.put(new ByteTag("ultrawarm", (byte) 0));
        overworldTag.put(new ByteTag("has_ceiling", (byte) 0));
        return overworldTag;
    }

    private static CompoundTag getPlainsTag() {
        CompoundTag plainsTag = new CompoundTag("");
        plainsTag.put(new StringTag("name", "minecraft:plains"));
        plainsTag.put(new StringTag("precipitation", "rain"));
        plainsTag.put(new FloatTag("depth", 0.125f));
        plainsTag.put(new FloatTag("temperature", 0.8f));
        plainsTag.put(new FloatTag("scale", 0.05f));
        plainsTag.put(new FloatTag("downfall", 0.4f));
        plainsTag.put(new StringTag("category", "plains"));

        CompoundTag effects = new CompoundTag("effects");
        effects.put(new LongTag("sky_color", 7907327));
        effects.put(new LongTag("water_fog_color", 329011));
        effects.put(new LongTag("fog_color", 12638463));
        effects.put(new LongTag("water_color", 4159204));

        CompoundTag moodSound = new CompoundTag("mood_sound");
        moodSound.put(new IntTag("tick_delay", 6000));
        moodSound.put(new FloatTag("offset", 2.0f));
        moodSound.put(new StringTag("sound", "minecraft:ambient.cave"));
        moodSound.put(new IntTag("block_search_extent", 8));

        effects.put(moodSound);

        plainsTag.put(effects);

        return plainsTag;
    }

    private static CompoundTag convertToValue(String name, int id, Map<String, Tag> values) {
        CompoundTag tag = new CompoundTag(name);
        tag.put(new StringTag("name", name));
        tag.put(new IntTag("id", id));
        CompoundTag element = new CompoundTag("element");
        element.setValue(values);
        tag.put(element);

        return tag;
    }
}
