package com.changgou.file.util;

import com.changgou.file.pojo.FastDFSFile;
import org.csource.common.MyException;
import org.csource.common.NameValuePair;
import org.csource.fastdfs.*;
import org.springframework.core.io.ClassPathResource;

import java.io.ByteArrayInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * @ClassName com.changgou.file.util.FastDFSUtil
 * @Description 文件操作类
 * @Author Maid
 * @Date 2020/7/9 0009 15:45
 * @Version v1.0
 */
public class FastDFSUtil {
    /***
     * 初始化加载FastDFS的TrackerServer配置
     */
    static {
        try {
            String filePath = new ClassPathResource("fdfs_client.conf").getFile().getAbsolutePath();
            ClientGlobal.init(filePath);
        } catch (IOException | MyException e) {
            e.printStackTrace();
        }
    }

    public static String[] upload(FastDFSFile file) throws Exception {
        //meta:
        NameValuePair[] meta_list = new NameValuePair[1];
        meta_list[0] = new NameValuePair("author",file.getAuthor());


        TrackerClient trackerClient = new TrackerClient();
        TrackerServer trackerServer = trackerClient.getConnection();
        StorageClient storageClient = new StorageClient(trackerServer, null);

        //[文件上传存储的Storage的名字group1，文件存储到Storage的文件名]
        String[] uploads = storageClient.upload_file(file.getContent(), file.getExt(), meta_list);
        return uploads;

    }


    /**
     * 获取文件信息
     * @param groupName             group1
     * @param remoteFileName        M00/00/00/wKgBB18JFLqAUo2dAAd8Z2CzvPY160.jpg
     * @return FileInfo
     * @throws Exception
     */
    public static FileInfo getFile(String groupName, String remoteFileName) throws Exception {

        TrackerClient trackerClient = new TrackerClient();
        TrackerServer trackerServer = trackerClient.getConnection();
        StorageClient storageClient = new StorageClient(trackerServer, null);

        return storageClient.get_file_info(groupName,remoteFileName);
    }


    /**
     * 下载文件
     * @param groupName             group1
     * @param remoteFileName        M00/00/00/wKgBB18JFLqAUo2dAAd8Z2CzvPY160.jpg
     * @return
     * @throws Exception
     */
    public static InputStream downloadFile(String groupName, String remoteFileName) throws Exception {

        TrackerClient trackerClient = new TrackerClient();
        TrackerServer trackerServer = trackerClient.getConnection();
        StorageClient storageClient = new StorageClient(trackerServer, null);

        byte[] buffer = storageClient.download_file(groupName,remoteFileName);

        return new ByteArrayInputStream(buffer);
    }


    /**
     * 删除文件
     * @param groupName             group1
     * @param remoteFileName        M00/00/00/wKgBB18JFLqAUo2dAAd8Z2CzvPY160.jpg
     * @throws Exception
     */
    public static void deleteFile(String groupName, String remoteFileName) throws Exception {

        TrackerClient trackerClient = new TrackerClient();
        TrackerServer trackerServer = trackerClient.getConnection();
        StorageClient storageClient = new StorageClient(trackerServer, null);

        storageClient.delete_file(groupName,remoteFileName);
    }

    /***
     * 获取Storage组
     * @param groupName
     * @return
     * @throws IOException
     */
    public static StorageServer getStoreStorages(String groupName)
            throws IOException {
        //创建TrackerClient
        TrackerClient trackerClient = new TrackerClient();
        //获取TrackerServer
        TrackerServer trackerServer = trackerClient.getConnection();
        //获取Storage组
        return trackerClient.getStoreStorage(trackerServer, groupName);
    }

    public static void main(String[] args) throws Exception {

        StorageServer storageServer = getStoreStorages("group1");
        System.out.println(storageServer.getStorePathIndex());
        System.out.println(storageServer.getInetSocketAddress().getHostString());
    }
}
