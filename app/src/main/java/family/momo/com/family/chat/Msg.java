package family.momo.com.family.chat;
public class Msg {
    public static final int TYPE_RECEIVED = 0;// 接收消息
    public static final int TYPE_SENT = 1;// 发送消息

    private String content;
    private String name;
    boolean isGroup = false;

    public void setGroup(boolean isGroup) {
        this.isGroup = isGroup;
    }

    public boolean isGroup() {
        return isGroup;
    }


    private int type;

    public Msg(String content, int type) {
        this.content = content;
        this.type = type;
    }
    public Msg(String content,String name, int type) {
        this.content = content;
        this.type = type;
        this.name = name;
    }
    public Msg(String content,String name, int type,boolean isGroup) {
        this.content = content;
        this.type = type;
        this.name = name;
        this.isGroup = isGroup;
    }

    public static int getTypeReceived() {
        return TYPE_RECEIVED;
    }

    public static int getTypeSent() {
        return TYPE_SENT;
    }

    public String getContent() {
        return content;
    }
    public String getName() {
        return name;
    }


    public void setContent(String content) {
        this.content = content;
    }
    public void setName(String name) {
        this.name = name;
    }


    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}