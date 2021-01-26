package com.seeker.fitness.all.util.mongodb;

import com.mongodb.*;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.gridfs.GridFS;
import com.mongodb.gridfs.GridFSDBFile;
import com.mongodb.gridfs.GridFSInputFile;
import com.seeker.fitness.all.util.PracticalUtil;
import org.apache.commons.codec.digest.DigestUtils;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

@Configuration
public class MongoFactory {
    /*
    常见的配置参数：
    connectionsPerHost：每个主机的连接数
    threadsAllowedToBlockForConnectionMultiplier：线程队列数，它以上面connectionsPerHost值相乘的结果就是线程队列最大值。如果连接线程排满了队列就会抛出“Out of semaphores to get db”错误。
    maxWaitTime:最大等待连接的线程阻塞时间
    connectTimeout：连接超时的毫秒。0是默认和无限
    socketTimeout：socket超时。0是默认和无限
    autoConnectRetry：这个控制是否在一个连接时，系统会自动重试
     */
    private static MongoClient mongoClient;
    private static String  ip;
    private static Integer connectionsPerHost;
    private static Integer threadsAllowedToBlockForConnectionMultiplier;
    private static Integer maxWaitTime;
    private static Integer connectTimeout;
    private static String password;
    @Value("${mongodb.ip}")
    private void setIp(String ip) {
        MongoFactory.ip = ip;
    }
    @Value("${mongodb.connectionsPerHost}")
    private void setConnectionsPerHost(Integer connectionsPerHost) {
        MongoFactory.connectionsPerHost = connectionsPerHost;
    }
    @Value("${mongodb.threadsAllowedToBlockForConnectionMultiplier}")
    private void setThreadsAllowedToBlockForConnectionMultiplier(Integer threadsAllowedToBlockForConnectionMultiplier) {
        MongoFactory.threadsAllowedToBlockForConnectionMultiplier = threadsAllowedToBlockForConnectionMultiplier;
    }
    @Value("${mongodb.maxWaitTime}")
    private void setMaxWaitTime(String maxWaitTime) {
        MongoFactory.maxWaitTime = PracticalUtil.parseTimeInt(maxWaitTime);
    }
    @Value("${mongodb.connectTimeout}")
    private void setConnectTimeout(String connectTimeout) {
        MongoFactory.connectTimeout = PracticalUtil.parseTimeInt(connectTimeout);
    }


    @PostConstruct
    private void init(){
        MongoClientOptions.Builder build = new MongoClientOptions.Builder();
        build.connectionsPerHost(connectionsPerHost);   //与目标数据库能够建立的最大connection数量为50
        build.threadsAllowedToBlockForConnectionMultiplier(threadsAllowedToBlockForConnectionMultiplier); //如果当前所有的connection都在使用中，则每个connection上可以有50个线程排队等待
        /*
         * 一个线程访问数据库的时候，在成功获取到一个可用数据库连接之前的最长等待时间为2分钟
         * 这里比较危险，如果超过maxWaitTime都没有获取到这个连接的话，该线程就会抛出Exception
         * 故这里设置的maxWaitTime应该足够大，以免由于排队线程过多造成的数据库访问失败
         */
        build.maxWaitTime(maxWaitTime);//最长等待时间
        build.connectTimeout(connectTimeout); //与数据库建立连接的timeout设置为1分钟
        MongoClientOptions myOptions = build.build();
        try {
            //数据库连接实例
            mongoClient = new MongoClient(ip, myOptions);
            //gridFS实例
            gridFS=new GridFS(mongoClient.getDB(databaseName),collectionName);
        } catch (MongoException e){
            e.printStackTrace();
        }
    }

    /**
     * 获取一个数据库
     * @param databaseName
     * @return
     */
    public static MongoDatabase getMongoDatabase(String databaseName){
        return mongoClient.getDatabase(databaseName);
    }

    /**
     * 获取一个集合
     * @param databaseName
     * @param collectionName
     * @return
     */
    public static MongoCollection<Document> getCollection(String databaseName,String collectionName){
        return getMongoDatabase(databaseName).getCollection(collectionName);
    }




    //GridFS相关---------------------------------------------------------------------------
    private static String databaseName;
    private static String collectionName;
    private static GridFS gridFS;

    @Value("${mongodb.gridFS.databaseName}")
    private void setDatabaseName(String databaseName) {
        MongoFactory.databaseName = databaseName;
    }

    @Value("${mongodb.gridFS.cllectionName}")
    private void setCllectionName(String collectionName) {
        MongoFactory.collectionName = collectionName;
    }

    /**
     * 获取一个grid需要的数据库
     * @return
     */
//    public static DB getDB(){
//        return mongoClient.getDB(databaseName);
//    }

//    public static void find(GridFSDBFile gridFSDBFile) throws IOException {
//        if(gridFSDBFile!=null){
//            System.out.println("id:"+gridFSDBFile.getId());
//            System.out.println("fileName:"+gridFSDBFile.getFilename());
//            System.out.println("size:"+gridFSDBFile.getChunkSize());
//            System.out.println("length:"+gridFSDBFile.getLength());
//            System.out.println("uploadDate:"+gridFSDBFile.getUploadDate());
//            System.out.println("----------------------------------------------------");
//        }
//    }

    /**
     * 保存文件
     * @param file
     * @param fileName
     * @throws IOException
     */
    public static void save(File file, String fileName) throws IOException {
        save(new FileInputStream(file),fileName);
    }
    /**
     * 保存文件
     * 由于我们的文件id都是利用该文件的md5值存储的 所以同样的文件他们的id一定是一样的
     * 保存前利用id先查询一次是否已经有一样的文件了 如果找不到再进行保存 否则不保存 这样可以避免重复存储
     * @param in
     * @param fileName
     */
    public static void save(FileInputStream in,String fileName) throws IOException {
        Object id= DigestUtils.md5Hex(in);
        GridFSDBFile gridFSDBFile=getFileById(id);
        if(gridFSDBFile==null){
            //创建一个GridFSInputFile
            GridFSInputFile gridFSInputFile=gridFS.createFile(in);
            //设置id
            gridFSInputFile.setId(id);
            //设置文件名
            gridFSInputFile.setFilename(fileName);
//            gridFSInputFile.setChunkSize();
//            gridFSInputFile.setContentType();
//            gridFSInputFile.setMetaData();
            //保存操作
            gridFSInputFile.save();
        }
    }

    /**
     * 根据id查询文件
     * @param id
     * @return
     */
    public static GridFSDBFile getFileById(Object id){
        BasicDBObject basicDBObject=new BasicDBObject("_id",id);
        GridFSDBFile gridFSDBFile=gridFS.findOne(basicDBObject);
        return gridFSDBFile;
    }

    /**
     * 根据fileName查询文件
     * @param fileName
     * @return
     */
    public static GridFSDBFile getFileByFileName(String fileName){
        BasicDBObject basicDBObject=new BasicDBObject("filename",fileName);
        GridFSDBFile gridFSDBFile=gridFS.findOne(basicDBObject);
        return gridFSDBFile;
    }

    /**
     * 删除满足条件的文件
     * @param filesQuery
     */
    public static void deleteFile(Document filesQuery){
        MongoDatabase mongoDatabase=getMongoDatabase(databaseName);
        MongoCollection<Document> files= mongoDatabase.getCollection(collectionName+".files");
        MongoCollection<Document> chunks= mongoDatabase.getCollection(collectionName+".chunks");
        //找到符合给定条件的所有文档
        FindIterable<Document> findIterable =files.find(filesQuery);
        //循环删除这些文档以及对应的chunks
        for(Document document:findIterable){
            //将查询回来的每一个符合条件的文档的id取出并赋值给chunksQuery作为删除的依据
            Document chunksQuery=new Document("files_id",document.get("_id"));
            //删除此文档的chunks部分
            chunks.deleteMany(chunksQuery);
            //删除此文档
            files.deleteOne(document);
        }

    }

    public static void main(String[] args) {
        int flag=0;
        int[] arr=new int[]{2,5,4,9,8};
        for(int i=0;i<arr.length;i++){
            for(int o=i+1;o<arr.length;o++){
                if(arr[i]>arr[o]){
                    int t=arr[i];
                    arr[i]=arr[o];
                    arr[o]=t;
                }
                flag++;
            }
        }
        System.out.println("yi一共比了"+flag+"次");
        for(int i:arr){
            System.out.print(i);
        }
        System.out.println();
        flag=0;
        int[] arr2=new int[]{2,5,4,9,8};
        for(int i=1;i<arr2.length;i++){
            for(int o=0;o<arr2.length-i;o++){
                if(arr2[o]>arr2[o+1]){
                    int t=arr2[o];
                    arr2[o]=arr2[o+1];
                    arr2[o+1]=t;
                }
                flag++;
            }
        }
        System.out.println("yi一共比了"+flag+"次");
        for(int i:arr2){
            System.out.print(i);
        }
    }
}
