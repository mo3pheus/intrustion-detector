package hungry.monkey.intrusion.detector.intrusion.monitor;

import hungry.monkey.intrusion.detector.db.IpDataRepository;
import hungry.monkey.intrusion.detector.db.UserDataRepository;
import hungry.monkey.intrusion.detector.domain.IpData;
import hungry.monkey.intrusion.detector.domain.UserData;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class IntrusionAlarm {
    private Map<String, Integer> ipHistogram;
    private Map<String, Integer> userHistogram;

    @Value("${key.words}")
    private List<String> keyWords;

    @Value("${target.file}")
    private String targetFile;

    private DateTime currentDate;
    private Integer linesRead;

    @Autowired
    private UserDataRepository userDataRepository;

    @Autowired
    private IpDataRepository ipDataRepository;

    @Scheduled(fixedRate = 5000)
    private void analyzeTarget() {
        try {
            List<String> lines = Files.readAllLines(Paths.get(targetFile));
            if (lines.size() < linesRead) {
                log.info("Reading new log file, resetting counters");
                initializeResources();
            } else {
                log.info("Lines read = " + linesRead);
            }

            if (linesRead == (lines.size() - 1)) {
                log.info("No new content");
                return;
            }

            for (int i = linesRead; i < lines.size(); i++) {
                String line = lines.get(i);
                if (line.contains("Failed password")) {
                    String[] words = line.split(" ");
                    updateUserMap(words, "for");
                    updateUserMap(words, "user");
                    updateIpMap(words);
                }
            }
            linesRead = lines.size() - 1;

            showIpHistogram();
            showUserHistogram();
            addUsers();
            addIpAddresses();
        } catch (IOException e) {
            log.error("Could not load target file." + targetFile, e);
        }
    }

    private void addUsers() {
        for (String userName : this.userHistogram.keySet()) {
            Integer count = userHistogram.get(userName);

            UserData searchedUser = userDataRepository.findByUserName(userName);

            if (searchedUser != null) {
                searchedUser.setCount(count);
                userDataRepository.save(searchedUser);
            } else {
                UserData userData = new UserData();
                userData.setUserName(userName);
                userData.setCount(count);
                userDataRepository.save(userData);
            }
        }
    }

    private void addIpAddresses() {
        for (String ip : ipHistogram.keySet()) {
            Integer count = ipHistogram.get(ip);

            IpData searchedIp = ipDataRepository.findByIpAddress(ip);
            if (searchedIp != null) {
                searchedIp.setCount(count);
                ipDataRepository.save(searchedIp);
            } else {
                IpData ipData = new IpData();
                ipData.setIpAddress(ip);
                ipData.setCount(count);

                ipDataRepository.save(ipData);
            }
        }
    }

    private void updateIpMap(String[] words) {
        Integer fromIndex = findIndex("from", words);
        if (fromIndex != null && (fromIndex < (words.length - 1))) {
            String ip = words[fromIndex + 1];
            log.info("Intruding ip " + ip);
            if (ipHistogram.containsKey(ip)) {
                Integer loginCount = ipHistogram.get(ip);
                ipHistogram.put(ip, loginCount + 1);
            } else {
                ipHistogram.put(ip, 1);
            }
        }
    }

    private void updateUserMap(String[] words, String keyword) {
        Integer userIndex = findIndex(keyword, words);
        if (userIndex != null && (userIndex < (words.length - 1))) {
            String user = words[userIndex + 1];
            log.info("Intruding user " + user);
            if (userHistogram.containsKey(user)) {
                Integer loginCount = userHistogram.get(user);
                userHistogram.put(user, loginCount + 1);
            } else {
                userHistogram.put(user, 1);
            }
        }
    }

    private Integer findIndex(String keyWord, String[] words) {
        for (int i = 0; i < words.length; i++) {
            if (words[i].equals(keyWord)) {
                return i;
            }
        }

        return null;
    }

    public Map<String, Integer> getIpHistogram() {
        return this.ipHistogram;
    }

    public Map<String, Integer> getUserHistogram() {
        return this.userHistogram;
    }

    private void initializeResources() {
        this.userHistogram = new HashMap<>();
        this.ipHistogram = new HashMap<>();
        this.linesRead = 0;
    }

    private void showUserHistogram() {
        for (String user : userHistogram.keySet()) {
            log.debug("User :: " + user + " loginCount :: " + userHistogram.get(user));
        }
    }

    private void showIpHistogram() {
        for (String ip : ipHistogram.keySet()) {
            log.debug("IP :: " + ip + " loginCount :: " + ipHistogram.get(ip));
        }
    }

    @PostConstruct
    private void initialize() {
        initializeResources();
    }
}
