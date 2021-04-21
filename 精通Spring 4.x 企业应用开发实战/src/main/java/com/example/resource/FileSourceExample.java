package com.example.resource;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.PathResource;
import org.springframework.core.io.WritableResource;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * 从文件系统读取资源
 */
public class FileSourceExample {

    public static void main(String[] args) throws IOException {
        String filePath = "D:/upload/file.txt";

        // 使用系统文件路径方式加载文件
        WritableResource resource = new PathResource(filePath);

        // 使用类路径方式加载文件
        ClassPathResource classPathResource = new ClassPathResource("test-demo.txt");

        // 使用 WritableResource 接口写资源文件
        OutputStream outputStream = resource.getOutputStream();
        outputStream.write("hello world".getBytes());
        outputStream.close();

        // 使用 Resource 接口读资源文件
        InputStream inputStream = resource.getInputStream();
        InputStream classPathResourceInputStream = classPathResource.getInputStream();

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        int i;
        while((i = inputStream.read()) != -1) {
            byteArrayOutputStream.write(i);
        }
        System.out.println(byteArrayOutputStream.toString());

        byteArrayOutputStream.reset();
        while((i = classPathResourceInputStream.read()) != -1) {
            byteArrayOutputStream.write(i);
        }
        System.out.println(byteArrayOutputStream.toString());

        System.out.println("res1:" + resource.getFilename());
        System.out.println("res2:" + classPathResource.getFilename());

    }
}
