package com.snoopy.mgdb.main;

import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by  on 2016/12/22.
 */
public class MongoDBJDBC {
    public static void main(String[] args) {
        try{
        //连接到MongoDB服务，如果是远程连接可以替换“localhost”为服务器所在IP地址
        // ServerAddress()两个参数分别为服务器地址和端口
            ServerAddress serverAddress = new ServerAddress("localhost",27017);
            List<ServerAddress> addresses = new ArrayList<ServerAddress>();
            addresses.add(serverAddress);
            //MongoCredential.createScramSha1Creadential()三个参数分别是用户名数据库名称密码
            MongoCredential credential = MongoCredential.createScramSha1Credential(
                    "username", "databaseName", "password".toCharArray());
            List<MongoCredential> credentials  = new ArrayList<MongoCredential>();
            credentials.add(credential);
            //通过连接认证获取MongoDB连接
//            MongoClient mongoClient = new MongoClient(addresses,credentials);
            MongoClient mongoClient = new MongoClient(addresses);

            //连接到数据库
            MongoDatabase mongoDatabase = mongoClient.getDatabase("unionpay");
            System.out.println("Connect to database successfully");

//            //创建集合
//            mongoDatabase.createCollection("unionpay_message");
//            System.out.println("unionpay_message集合创建成功");

            //获取集合
            MongoCollection<Document> collection = mongoDatabase.getCollection("unionpay_message");
            System.out.println("集合unionpay_message选择成功");

            //插入文档
            /***
             * 1.创建文档org.bson.Document参数为key-value的格式
             * 2.创建文档集合List<Document>
             * 3.将文档集合插入数据库集合中mongoCollection.insertMang(List<Document>)插入单
             * 个文档可以用mongoCollection.insertOne(Document)
             * **/
//             Document document = new Document("title","MongoDB").append("description","database")
//                     .append("likes",100).append("by","Fly");
//            List<Document> documents = new ArrayList<Document>();
//            documents.add(document);
//            collection.insertMany(documents);
//            System.out.println("文档插入成功");

            //检索所有的文档
            /***
             * 1.获取迭代器FindIterable<Document>
             * 2.获取游标MongoCursor<Document>
             * 3.通过游标遍历检索出的文档集合
             */
            FindIterable<Document> findIterable = collection.find();
            MongoCursor<Document> mongoCursor = findIterable.iterator();
            while (mongoCursor.hasNext()){
                System.out.println(mongoCursor.next());
            }

            //更新文档 将文档中likes = 100的文档修改为likes = 200
            collection.updateMany(Filters.eq("likes",100),new Document("$set",new Document("likes",200)));
            //检索查看结果

            FindIterable<Document> findIterableUpdate = collection.find();
            MongoCursor<Document> mongoCursorUpdate = findIterableUpdate.iterator();
            while (mongoCursorUpdate.hasNext()){
                System.out.println(mongoCursorUpdate.next());
            }


            //删除符合条件的第一个文档
//            collection.deleteOne(Filters.eq("likes",200));
            //删除所有符合条件的文档
            collection.deleteMany(Filters.eq("likes",200));
            //检索查看结果
            FindIterable<Document> findIterableAfterDelete = collection.find();
            MongoCursor<Document>  mongoCursorAfterDelete = findIterableAfterDelete.iterator();
            while (mongoCursorAfterDelete.hasNext()){
                System.out.println(mongoCursorAfterDelete.next());
            }

        }catch (Exception e){
            System.err.println(e.getClass().getName()+":"+e.getMessage());
        }
    }
}
