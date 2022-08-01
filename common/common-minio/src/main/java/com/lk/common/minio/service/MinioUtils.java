package com.lk.common.minio.service;

import io.minio.*;
import io.minio.http.Method;
import io.minio.messages.Bucket;
import io.minio.messages.DeleteError;
import io.minio.messages.DeleteObject;
import io.minio.messages.Item;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;


/**
 * @Date 2021/11/18
 */
@Slf4j
public class MinioUtils {

    private MinioClient minioClient;

    public MinioUtils(MinioClient minioClient) {
        this.minioClient = minioClient;
    }

    /**
     * 检查存储桶是否存在
     *
     * @param bucketName 存储桶名称
     * @return
     */
    @SneakyThrows
    public boolean bucketExists(String bucketName) {
        boolean flag = minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
        if (flag) {
            return true;
        }
        return false;
    }

    /**
     * 创建存储桶
     *
     * @param bucketName 存储桶名称
     */
    @SneakyThrows
    public boolean makeBucket(String bucketName) {
        boolean flag = bucketExists(bucketName);
        if (!flag) {
            minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
            return true;
        } else {
            return false;
        }
    }

    /**
     * 列出所有存储桶名称
     *
     * @return
     */
    @SneakyThrows
    public List<String> listBucketNames() {
        List<Bucket> bucketList = listBuckets();
        List<String> bucketListName = new ArrayList<>();
        for (Bucket bucket : bucketList) {
            bucketListName.add(bucket.name());
        }
        return bucketListName;
    }

    /**
     * 列出所有存储桶
     *
     * @return
     */
    @SneakyThrows
    public List<Bucket> listBuckets() {
        return minioClient.listBuckets();
    }

    /**
     * 删除存储桶
     *
     * @param bucketName 存储桶名称
     * @return
     */
    @SneakyThrows
    public boolean removeBucket(String bucketName) {
        boolean flag = bucketExists(bucketName);
        if (flag) {
            Iterable<Result<Item>> myObjects = listObjects(bucketName);
            for (Result<Item> result : myObjects) {
                Item item = result.get();
                // 有对象文件，则删除失败
                if (item.size() > 0) {
                    return false;
                }
            }
            // 删除存储桶，注意，只有存储桶为空时才能删除成功。
            minioClient.removeBucket(RemoveBucketArgs.builder().bucket(bucketName).build());
            flag = bucketExists(bucketName);
            if (!flag) {
                return true;
            }

        }
        return false;
    }

    /**
     * 列出存储桶中的所有对象名称
     *
     * @param bucketName 存储桶名称
     * @return
     */
    @SneakyThrows
    public List<String> listObjectNames(String bucketName) {
        List<String> listObjectNames = new ArrayList<>();
        boolean flag = bucketExists(bucketName);
        if (flag) {
            Iterable<Result<Item>> myObjects = listObjects(bucketName);
            for (Result<Item> result : myObjects) {
                Item item = result.get();
                listObjectNames.add(item.objectName());
            }
        }
        return listObjectNames;
    }

    /**
     * 列出存储桶中的所有对象
     *
     * @param bucketName 存储桶名称
     * @return
     */
    @SneakyThrows
    public Iterable<Result<Item>> listObjects(String bucketName) {
        boolean flag = bucketExists(bucketName);
        if (flag) {
            return minioClient.listObjects(ListObjectsArgs.builder().bucket(bucketName).build());
        }
        return null;
    }

    /**
     * 通过文件上传到对象
     *
     * @param bucketName 存储桶名称
     * @param objectName 存储桶里的对象名称
     * @param fileName   File name
     * @return
     */
    @SneakyThrows
    public boolean putObject(String bucketName, String objectName, String fileName) {
        boolean flag = bucketExists(bucketName);
        if (flag) {
            UploadObjectArgs build = UploadObjectArgs.builder().bucket(bucketName).object(objectName).filename(fileName).build();
            minioClient.uploadObject(build);
            StatObjectResponse statObject = statObject(bucketName, objectName);
            if (statObject != null && statObject.size() > 0) {
                return true;
            }
        }
        return false;

    }


    /**
     * 通过InputStream上传对象
     *
     * @param bucketName 存储桶名称
     * @param objectName 存储桶里的对象名称
     * @param stream     要上传的流
     * @param stream     要上传的文件类型 MimeTypeUtils.IMAGE_JPEG_VALUE
     * @return
     */
    @SneakyThrows
    public boolean putObject(String bucketName, String objectName, InputStream stream, String contentType) {
        boolean flag = bucketExists(bucketName);
        if (flag) {
            PutObjectArgs putObjectArgs = PutObjectArgs.builder().bucket(bucketName).object(objectName).stream(stream, stream.available(), -1).contentType(contentType).build();
            minioClient.putObject(putObjectArgs);
            StatObjectResponse statObject = statObject(bucketName, objectName);
            if (statObject != null && statObject.size() > 0) {
                return true;
            }
        }
        return false;
    }

    /**
     * 以流的形式获取一个文件对象
     *
     * @param bucketName 存储桶名称
     * @param objectName 存储桶里的对象名称
     * @return
     */
    @SneakyThrows
    public InputStream getObject(String bucketName, String objectName) {
        boolean flag = bucketExists(bucketName);
        if (flag) {
            StatObjectResponse statObject = statObject(bucketName, objectName);
            if (statObject != null && statObject.size() > 0) {
                GetObjectArgs getObjectArgs = GetObjectArgs.builder().bucket(bucketName).object(objectName).build();
                return minioClient.getObject(getObjectArgs);
            }
        }
        return null;
    }

    /**
     * 以流的形式获取一个文件对象（断点下载）
     *
     * @param bucketName 存储桶名称
     * @param objectName 存储桶里的对象名称
     * @param offset     起始字节的位置
     * @param length     要读取的长度 (可选，如果无值则代表读到文件结尾)
     * @return
     */
    @SneakyThrows
    public InputStream getObject(String bucketName, String objectName, long offset, Long length) {
        boolean flag = bucketExists(bucketName);
        if (flag) {
            StatObjectResponse statObject = statObject(bucketName, objectName);
            if (statObject != null && statObject.size() > 0) {
                GetObjectArgs getObjectArgs = GetObjectArgs.builder().bucket(bucketName).object(objectName).offset(offset).length(length).build();
                return minioClient.getObject(getObjectArgs);
            }
        }
        return null;
    }

    /**
     * 删除一个对象
     *
     * @param bucketName 存储桶名称
     * @param objectName 存储桶里的对象名称
     */
    @SneakyThrows
    public boolean removeObject(String bucketName, String objectName) {
        boolean flag = bucketExists(bucketName);
        if (flag) {
            minioClient.removeObject(RemoveObjectArgs.builder().bucket(bucketName).object(objectName).build());
            return true;
        }
        return false;
    }

    /**
     * 删除指定桶的多个文件对象,返回删除错误的对象列表，全部删除成功，返回空列表
     *
     * @param bucketName  存储桶名称
     * @param objectNames 含有要删除的多个object名称的迭代器对象
     * @return
     */
    @SneakyThrows
    public List<String> removeObjects(String bucketName, List<String> objectNames) {
        List<String> deleteErrorNames = new ArrayList<>();
        boolean flag = bucketExists(bucketName);
        if (flag) {
            List<DeleteObject> objects = new ArrayList<>();
            for (String objectName : objectNames) {
                objects.add(new DeleteObject(objectName));
            }
            Iterable<Result<DeleteError>> results = minioClient.removeObjects(RemoveObjectsArgs.builder().bucket(bucketName).objects(objects).build());
            for (Result<DeleteError> result : results) {
                DeleteError error = result.get();
                deleteErrorNames.add(error.objectName());
            }
        }
        return deleteErrorNames;
    }

    /**
     * 生成一个给HTTP GET请求用的presigned URL。
     * 浏览器/移动端的客户端可以用这个URL进行下载，即使其所在的存储桶是私有的。这个presigned URL可以设置一个失效时间，默认值是7天。
     *
     * @param bucketName 存储桶名称
     * @param objectName 存储桶里的对象名称
     * @param expires    失效时间（以秒为单位），默认是7天，不得大于七天
     * @return
     */
    @SneakyThrows
    public String presignedObjectUrl(String bucketName, String objectName, Integer expires) {
        boolean flag = bucketExists(bucketName);
        String url = "";
        if (flag) {
            url = minioClient.getPresignedObjectUrl(GetPresignedObjectUrlArgs.builder().bucket(bucketName).object(objectName).expiry(expires).method(Method.GET).build());
        }
        return url;
    }

    /**
     * 获取对象的元数据
     *
     * @param bucketName 存储桶名称
     * @param objectName 存储桶里的对象名称
     * @return
     */
    @SneakyThrows
    public StatObjectResponse statObject(String bucketName, String objectName) {
        boolean flag = bucketExists(bucketName);
        if (flag) {
            return minioClient.statObject(StatObjectArgs.builder().bucket(bucketName).object(objectName).build());
        }
        return null;
    }


    /**
     * web下载
     *
     * @param bucketName   存储桶名称
     * @param objectName   对象名称
     * @param fileName     文件名称
     * @param response     输出对象
     */
    public void downloadFile(String bucketName, String objectName,String fileName, HttpServletResponse response) {
        ServletOutputStream servletOutputStream = null;
        GetObjectResponse getObject = null;
        try {
            getObject = minioClient.getObject(GetObjectArgs.builder().bucket(bucketName).object(objectName).build());
            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8").replaceAll("\\+", "%20"));
            servletOutputStream = response.getOutputStream();
            int len;
            byte[] buffer = new byte[1024];
            while ((len = getObject.read(buffer)) > 0) {
                servletOutputStream.write(buffer, 0, len);
            }
            servletOutputStream.flush();
            getObject.close();
            servletOutputStream.close();
        } catch (Exception e) {
            log.error("downloadFile error: bucketName = {} : {}", bucketName, e);
        }finally {
            try {
                if (servletOutputStream != null) {
                    servletOutputStream.close();
                }
                if (getObject != null) {
                    getObject.close();
                }
            } catch (IOException e) {
                log.error("上传文件关闭流异常", e);
            }

        }
    }
}
