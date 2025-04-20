package cn.izualzhy.pojo;

public enum SexEnum {
   MALE(1, "男"),
   FEMALE(2, "女");

   private int id ;
   private String name;

   SexEnum(int id, String name) {
      this.id = id;
      this.name= name;
   }

   public static SexEnum getSexById(int id) {
      for (SexEnum sex : SexEnum.values()) {
         if (sex.getId() == id) {
            return sex;
         }
      }
      return null;
   }

   public int getId() {
      return id;
   }
}