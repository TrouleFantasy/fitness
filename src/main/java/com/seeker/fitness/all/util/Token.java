package com.seeker.fitness.all.util;

import com.alibaba.fastjson.JSONObject;
import com.seeker.fitness.all.config.ConfigParamMapping;
import org.apache.commons.codec.digest.DigestUtils;

public  class Token {
    private String secret;
    private TokenHeader tokenHeader;
    private TokenPayload tokenPayload;
    //signature
    private String signature;
    
    public Token(){
        tokenHeader=new TokenHeader();
        tokenPayload=new TokenPayload();
        secret=ConfigParamMapping.getSecret();
    }
    
    public String toTokenString(){
        String header=tokenHeader.toBase64String();
        String payload=tokenPayload.toBase64String();
        signature=DigestUtils.sha256Hex(header+"."+payload+secret);
        return header+"."+payload+"."+signature;
    }

    public String getSignature() {
        return signature;
    }

    //header
    public String getTyp() {
        return tokenHeader.typ;
    }

    public void setTyp(String typ) {
        tokenHeader.typ = typ;
    }

    public String getAlg() {
        return tokenHeader.alg;
    }

    public void setAlg(String alg) {
        tokenHeader.alg = alg;
    }
    //payload
    public String getIss() {
        return tokenPayload.iss;
    }

    public void setIss(String iss) {
        tokenPayload.iss = iss;
    }

    public String getSub() {
        return tokenPayload.sub;
    }

    public void setSub(String sub) {
        tokenPayload.sub = sub;
    }

    public String getAud() {
        return tokenPayload.aud;
    }

    public void setAud(String aud) {
        tokenPayload.aud = aud;
    }

    public String getExp() {
        return tokenPayload.exp;
    }

    public void setExp(String exp) {
        tokenPayload.exp = exp;
    }

    public String getNbf() {
        return tokenPayload.nbf;
    }

    public void setNbf(String nbf) {
        tokenPayload.nbf = nbf;
    }

    public String getIat() {
        return tokenPayload.iat;
    }

    public void setIat(String iat) {
        tokenPayload.iat = iat;
    }

    public String getJti() {
        return tokenPayload.jti;
    }

    public void setJti(String jti) {
        tokenPayload.jti = jti;
    }

    public String getUserCode() {
        return tokenPayload.userCode;
    }

    public void setUserCode(String userCode) {
        tokenPayload.userCode = userCode;
    }
}

class TokenHeader{
    //header
    String typ;//token类型
    String alg;//加密方式

    public TokenHeader(){
        typ="JWT";
        alg="HS256";
    }

    public String toBase64String(){
        return PracticalUtil.base64Encoder(toJsonString());
    }

    public String toJsonString(){
        System.out.println(JSONObject.toJSONString(this));
        return JSONObject.toJSONString(this);
    }
}

class TokenPayload{
    //payload
    String iss;//发行者
    String sub;//主题
    String aud;//观众
    String exp;//过期时间
    String nbf;//Not before
    String iat;//发行时间
    String jti;//JWT ID
    String userCode;//用户名

    public String toBase64String(){
        return PracticalUtil.base64Encoder(toJsonString());
    }

    public String toJsonString(){
        System.out.println(JSONObject.toJSONString(this));
        return JSONObject.toJSONString(this);
    }
}
