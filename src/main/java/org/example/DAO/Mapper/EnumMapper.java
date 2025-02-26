package org.example.DAO.Mapper;

import org.example.Entity.Unity;

public class EnumMapper {
    public static Unity topicmaper(String topic){
        if(topic == null){
            return null;
        }

        return switch (topic){
            case  "G"-> Unity.G;
            case  "U"-> Unity.U;
            case  "L"-> Unity.L;
            default -> throw new RuntimeException("invalid topic: " + topic);
        };
    }
}
