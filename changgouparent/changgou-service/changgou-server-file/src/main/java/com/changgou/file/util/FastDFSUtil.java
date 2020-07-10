package com.changgou.file.util;

import com.changgou.file.pojo.FastDFSFile;
import org.csource.common.MyException;
import org.csource.common.NameValuePair;
import org.csource.fastdfs.ClientGlobal;
import org.csource.fastdfs.StorageClient;
import org.csource.fastdfs.TrackerClient;
import org.csource.fastdfs.TrackerServer;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;

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

    public static String[] upload(FastDFSFile file) throws IOException, MyException {
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
}
