package com.example.a0134598r.pathfinder.System;

/**
 * Created by jixiang on 3/9/15.
 */
public class JsonConstraints {

    public static String JSON_RESPONSE_ERROR = "error";
    public static String JSON_RESPONSE_SUCCESS = "success";

    public static String JSON_RESPONSE_ERROR_TYPE_NO_DOCTOR = "j0";
    public static String JSON_RESPONSE_ERROR_TYPE_NEED_REGISTER = "j1";

    public static  String JSON_RESPONSE_ERROR_TYPE_NO_CLINIC_NAME = "c0";
    public static  String JSON_RESPONSE_ERROR_TYPE_NO_CLINIC_NAME_REPLY = "at least tell me the clinic name";

    public static String JSON_RESPONSE_ERROR_TYPE_NO_INSURANCE_CARD = "i0";
    public static String JSON_RESPONSE_ERROR_TYPE_NO_INSURANCE_CARD_REPLY = "no insurance card avaliable";

    public static final String j_result = "result";

    public static final String JSON_RESPONSE_NULL_NOTICE = "No relative data";
    public static final String JSON_RESPONSE_ERROR_NOTICE = "HTTP return value is not 200";
    public static final String JSON_RESPONSE_PARAM_NULL_NOTICE = "some parameter you sent by http is null,plz debug and check it";

}
