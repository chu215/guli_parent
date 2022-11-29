package com.chu.vodtest;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.vod.model.v20170321.GetPlayInfoRequest;
import com.aliyuncs.vod.model.v20170321.GetPlayInfoResponse;

import java.util.List;

public class TestVod {

    public static void main(String[] args) {
        DefaultAcsClient client = InitObject.initVodClient("LTAI5tSesP2fwwJFdQYqFL82",
                "wNt8vkEXG8ilOAUgvfbiJOiIRodwJ0");

        GetPlayInfoRequest request = new GetPlayInfoRequest();
        GetPlayInfoResponse response = new GetPlayInfoResponse();

        List<GetPlayInfoResponse.PlayInfo> playInfoList = response.getPlayInfoList();
        for (GetPlayInfoResponse.PlayInfo playInfo : playInfoList) {
            System.out.println(playInfo.getPlayURL());
        }

        System.out.println(response.getVideoBase().getTitle());
    }
}
