package com.example.extractData;

import java.sql.Connection;

import com.example.dbConfig.DataBaseConfig;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

public class ExtractData {

    private Map<String, List> mapInfo = new HashMap<>();
    private List<String> names = new ArrayList<>();
    private List<String> hrefs = new ArrayList<>();
    private List<String> getData = new ArrayList<>();
    private List<String> experiences = new ArrayList<>();
    private DataBaseConfig dbComfig = new DataBaseConfig();
    private Statement stmt = null;

    private void getNameNInfo(String url) throws IOException, SQLException {
        try {
            Document document = Jsoup.connect(url).get();
            Elements baseUrlElements = document.select("#about-teachers-page.fake-menu.with-submenu div.container-fluid div#filterResult.container.position-relative div.row div.col-12.col-sm-6.col-md-4.col-lg-3.px-0 div.row.mx-0 div.col-12 div.teacher-card-vertical a.name.d-block");

            for (Element element : baseUrlElements) {
                String teacherNames = element.ownText();

                try {
                    insertIntoTeachersTable(teacherNames);
                } catch (SQLException e) {
                    e.printStackTrace();
                }

                names.add(teacherNames);
                String subUrls = element.attr("href");
                hrefs.add(subUrls);
                mapInfo.put("names", names);
                mapInfo.put("hrefs", hrefs);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(mapInfo.get("names"));
        System.out.println(mapInfo.get("hrefs"));
        retrieveExperiences(mapInfo);
    }

    public void retrieveExperiences(Map<String, List> mapInfo) throws IOException, SQLException {
        getData = mapInfo.get("hrefs");

        for (int i = 0; i <= getData.size(); i++) {
            Document document = Jsoup.connect(getData.get(i)).get();
            Elements subUrlElements = document.select("#about-teacher-page.fake-menu.with-submenu div.summery.container.mt-5 div.row.pt-5.awards div.col-12.col-md-6 div.h");

            if (subUrlElements.size() == 0) {
                System.out.printf("No experience existed");
                continue;
            }

            experiences.add(String.valueOf(subUrlElements.get(1).parentNode().childNodes()));
            String exp = experiences.toString().substring(20, 220).replaceAll("[\\s+ <p> </> <div> </div> nbs& h r ; , cla =]", " ");
            insertIntoRecordsTable(exp);
        }
    }

    private void insertIntoRecordsTable(String input) throws SQLException {
        Connection con = dbComfig.config();
        try {
            stmt = con.createStatement();

            String sql = "INSERT INTO test_db.records (experience_desc)" +
                    "VALUES(" + "'" + input.substring(35) + "'" + ")";

            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (stmt != null)
                con.close();
        }
    }

    public void insertIntoTeachersTable(String name) throws SQLException {
        Connection con = dbComfig.config();
        try {
            stmt = con.createStatement();

            String sql = "INSERT INTO test_db.teachers(teacher_name)" +
                    "VALUES(" + "'" + name + "'" + ")";

            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (stmt != null)
                con.close();
        }
    }

    public static void main(String[] args) throws IOException, SQLException {
        String url = "https://inverseschool.com/inverse-world/teachers/";
        ExtractData crawlerBase = new ExtractData();
        crawlerBase.getNameNInfo(url);
    }

}