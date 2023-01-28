package com.deliverylab.inspection.common.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Optional;
import java.util.Scanner;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class FileUtils {

    // 파일에 내용 저장하기
    public static void fileWriter(String path, String fileName, String insertStr, boolean add) throws Exception {
        File file = new File(path);

        if (!file.exists()) {
            // 경로가 없다면 생성합니다. (디렉토리)
            try {
                file.mkdirs();
            } catch (Exception e) {
                System.out.println("path mkdirs Error : " + e.toString());
            }
        }

        FileWriter writer = null;
        try {
            // 기존 파일의 내용에 이어서 쓰려면 true를
            // 기존 내용을 없애고 새로 쓰려면 false를 지정한다.
            writer = new FileWriter(file + "/" + fileName, add);
            writer.write(insertStr);

            writer.flush();

            System.out.println("file write 완료 ... ");
            System.out.println("file write 내용 : " + insertStr);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("fileWriter 에러 : " + e.toString());
        } finally {
            try {
                if (writer != null) {
                    writer.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    // 덮어쓰기
    public static void newFileWriter(String path, String fileName, String insertStr) throws Exception {
        fileWriter(path, fileName, insertStr, false);
    }

    // 이어쓰기
    public static void addFileWriter(String path, String fileName, String insertStr) throws Exception {
        fileWriter(path, fileName, insertStr, true);
    }

    // 파일 읽어서 내용 가저오기
    public static String fileReader(String path) throws Exception {
        String temp = "";

        try {
            File file = new File(path);
            log.info(path);

            Scanner sc = new Scanner(file);
            while (sc.hasNextLine()) {
                System.out.println(sc.nextLine());
                temp += sc.nextLine();
            }
            sc.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.out.println("fileReader 에러 : " + e.toString());
        }

        return temp;
    }

    public static String fileReaderOnTail(String path, int offset) throws Exception {
        try {
            // 1. RandomAcessFile, 마지막 라인을 담을 String, 읽을 라인 수
            RandomAccessFile randomAccessFile = new RandomAccessFile(path, "r");
            StringBuffer lastLine = new StringBuffer();

            // 2. 전체 파일 길이
            long fileLength = randomAccessFile.length();

            // 3. 포인터를 이용하여 뒤에서부터 앞으로 데이터를 읽는다.
            for (long pointer = fileLength - 1; pointer >= 0; pointer--) {

                // 3.1. pointer를 읽을 글자 앞으로 옮긴다.
                randomAccessFile.seek(pointer);

                // 3.2. pointer 위치의 글자를 읽는다.
                char c = (char) randomAccessFile.read();

                // 3.3. 줄바꿈이 3번(lineCount) 나타나면 더 이상 글자를 읽지 않는다.
                if (c == '\n') {
                    offset--;
                    if (offset == -1) {
                        break;
                    }
                }

                // 3.4. 결과 문자열의 앞에 읽어온 글자(c)를 붙여준다.
                lastLine.insert(0, c);
            }

            randomAccessFile.close();

            // 4. 결과 출력
            return lastLine.toString();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    public Optional<File> multipartFileToFile(MultipartFile file) throws IOException {
        File convertFile = new File(file.getOriginalFilename());
        if (convertFile.createNewFile()) {
            try (FileOutputStream fos = new FileOutputStream(convertFile)) {
                fos.write(file.getBytes());
            }
            return Optional.of(convertFile);
        }

        return Optional.empty();
    }
}
