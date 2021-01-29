package com.seeker.fitness.all.util;

import com.alibaba.fastjson.JSONObject;
import com.seeker.fitness.all.config.ConfigParamMapping;
import com.seeker.fitness.all.ex.IllegalTokenException;
import lombok.Data;
import org.apache.commons.codec.digest.DigestUtils;

public class Token {
    private static String secret= ConfigParamMapping.getSecret();
    private TokenHeader tokenHeader;
    private TokenPayload tokenPayload;
    //signature
    private String signature;

    public Token(){
        tokenHeader=new TokenHeader();
        tokenPayload=new TokenPayload();
    }
    public Token(TokenHeader tokenHeader,TokenPayload tokenPayload){
        this.tokenHeader=tokenHeader;
        this.tokenPayload=tokenPayload;
    }
    public Token(TokenHeader tokenHeader,TokenPayload tokenPayload,String signature){
        this.tokenHeader=tokenHeader;
        this.tokenPayload=tokenPayload;
        this.signature=signature;
    }
    public String toTokenString(){
        String header=tokenHeader.toBase64String();
        String payload=tokenPayload.toBase64String();
        signature=DigestUtils.sha256Hex(header+"."+payload+secret);
        return header+"."+payload+"."+signature;
    }

    /**
     * 将给定token 转换为token对象返回
     * @param token
     * @return
     */
    public static Token parseTokenObj(String token) throws IllegalTokenException{
        String[] tokenArr=token.split("\\.");
        if(tokenArr.length!=3){
            throw new IllegalTokenException("非法Token！");
        }
        String tokenHeaderStr= PracticalUtil.base64Decoder(tokenArr[0]);
        String tokenPayloadStr=PracticalUtil.base64Decoder(tokenArr[1]);
        String userSignature=tokenArr[2];
        TokenHeader tokenHeader=JSONObject.parseObject(tokenHeaderStr,TokenHeader.class);
        TokenPayload tokenPayload=JSONObject.parseObject(tokenPayloadStr,TokenPayload.class);
        Token tokenObj=new Token(tokenHeader,tokenPayload,userSignature);
        return tokenObj;
    }

    /**
     * 判断是否是本项目签发token
     * @param token
     * @return
     */
    public static boolean isTokenValid(String token){
        String[] tokenArr=token.split("\\.");
        String signature=DigestUtils.sha256Hex(tokenArr[0]+"."+tokenArr[1]+secret);
        if(signature.equals(tokenArr[2])){
            return true;
        }else{
            return false;
        }
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
@Data
class TokenHeader{
    //header
    String typ;//token类型
    String alg;//加密方式

    public TokenHeader(){
        typ="JWT";
        alg="SHA256Hex";
    }

    public String toBase64String(){
        return PracticalUtil.base64Encoder(toJsonString());
    }

    public String toJsonString(){
        return JSONObject.toJSONString(this);
}
    @Override
    public String toString() {
        return "TokenHeader{" +
                "typ='" + typ + '\'' +
                ", alg='" + alg + '\'' +
                '}';
    }
}
@Data
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
        return JSONObject.toJSONString(this);
    }

    @Override
    public String toString() {
        return "TokenPayload{" +
                "iss='" + iss + '\'' +
                ", sub='" + sub + '\'' +
                ", aud='" + aud + '\'' +
                ", exp='" + exp + '\'' +
                ", nbf='" + nbf + '\'' +
                ", iat='" + iat + '\'' +
                ", jti='" + jti + '\'' +
                ", userCode='" + userCode + '\'' +
                '}';
    }
}
