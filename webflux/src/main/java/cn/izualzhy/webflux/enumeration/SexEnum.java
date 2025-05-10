package cn.izualzhy.webflux.enumeration;

public enum SexEnum {
    MALE(1, "男"),
    FEMALE(0, "女");
    private int code;
    private String name;

    SexEnum(int code, String name) {
        this.code = code;
        this.name = name;
    }

    public static SexEnum getSexEnum(int code) {
        SexEnum [] enums = SexEnum.values();
        for (SexEnum item : enums) {
            if (item.getCode() == code) {
                return item;
            }
        }
        return null;
    }

    public int getCode() {
        return code;
    }
    public void setCode(int code) {
        this.code = code;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
}