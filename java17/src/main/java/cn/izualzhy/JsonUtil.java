package cn.izualzhy;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class JsonUtil {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static <T> List<Map<String, Object>> convertListToMapList(List<T> dataList) {
        return dataList.stream()
                .map(data -> objectMapper.convertValue(data, new com.fasterxml.jackson.core.type.TypeReference<Map<String, Object>>() {})) // 显式类型转换
                .collect(Collectors.toList());
    }

    public static class User {
        private String name;
        private int age;
        private String email;
    }

    public static void main(String[] args) {
        List<User> users = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            User user = new User();
            user.name = "name" + i;
            user.age = i;
            user.email = "email" + i;
            users.add(user);
        }

        List<Map<String, Object>> mapList = JsonUtil.convertListToMapList(users);
        System.out.println(mapList);
    }
}
