package server.dao;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * [SFR-1.6 & 3.5] 보고서 및 로그 관리 DAO
 * - 요금 변경 이력 기록
 * - 예외 보고서 조회
 */
public class ReportDAO {
    
    private static final String LOG_FILE = "data/price_logs.txt";

    /**
     * [SFR-1.6] 기본 요금 변경 시 변경 사유와 함께 기록
     * 저장 포맷: 시간,방번호,이전요금,새요금,변경사유
     * @param roomNumber
     * @param oldPrice
     * @param newPrice
     * @param reason
     */
    public void logPriceChange(int roomNumber, int oldPrice, int newPrice, String reason) {
        File file = new File(LOG_FILE);
        if (!file.exists()) {
            try {
                file.getParentFile().mkdirs();
                file.createNewFile();
            } catch (IOException e) {}
        }

        try (BufferedWriter bw = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream(file, true), StandardCharsets.UTF_8))) {
            
            // 현재 시간 포맷팅
            String time = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            
            // 로그 메시지 생성
            String log = String.format("%s,%d,%d,%d,%s", time, roomNumber, oldPrice, newPrice, reason);
            
            bw.write(log);
            bw.newLine();
            System.out.println("[ReportDAO] 요금 변경 로그 저장됨: " + log);
            
        } catch (IOException e) {
            System.err.println("[ReportDAO] 로그 저장 실패: " + e.getMessage());
        }
    }

    /**
     * [SFR-3.5] 요금 변경 예외 보고서 조회
     * 파일에 저장된 모든 로그를 읽어서 리스트로 반환
     * @return 
     */
    public List<String> getPriceChangeReport() {
        List<String> logs = new ArrayList<>();
        File file = new File(LOG_FILE);
        
        if (!file.exists()) {
            logs.add("기록된 변경 사항이 없습니다.");
            return logs;
        }

        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8))) {
            String line;
            while ((line = br.readLine()) != null) {
                logs.add(line);
            }
        } catch (Exception e) { 
        }
        return logs;
    }
}