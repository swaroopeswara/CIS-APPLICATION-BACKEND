package com.dw.ngms.cis.report;

import com.dw.ngms.cis.uam.configuration.ApplicationPropertiesConfiguration;
import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRSaver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.sql.DataSource;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;

@Slf4j
@Service
public class ReportGenerator {

    @Autowired
    private DataSource dataSource;
    @Autowired
    private ReportExporter reportExporter;


    @Autowired
    private ApplicationPropertiesConfiguration applicationPropertiesConfiguration;

    public boolean generateAndExportReport(String reportJrxml, String reportName, Map<String, Object> parameters) {
        JasperReport jasperReport = preparReport(reportJrxml);
        if (jasperReport == null) return false;
        JasperPrint jasperPrint = fillReport(jasperReport, parameters);
        if (jasperPrint == null) return false;
        reportExporter.exportToPdf(jasperPrint, reportName, "CIS");
        return true;
    }//generateReport

    private JasperReport preparReport(String reportJrxml) {
        if (reportJrxml == null) return null;
        try {
            InputStream reportStream = new FileInputStream(reportJrxml);
            JasperReport jasperReport = JasperCompileManager.compileReport(reportStream);
            JRSaver.saveObject(jasperReport, getJasperFilePath(reportJrxml));
            return jasperReport;
        } catch (Exception ex) {
            log.error("Error while preparing the report {}", ex.getMessage());
        }
        return null;
    }//preparReport

    private JasperPrint fillReport(JasperReport jasperReport, Map<String, Object> parameters) {
        if (jasperReport == null || CollectionUtils.isEmpty(parameters)) return null;
        JasperPrint jasperPrint = null;
        Connection con = null;
        try {
            con = dataSource.getConnection();
            jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, con);
        } catch (Exception ex) {
            log.error("Error while filling the report {}", ex.getMessage());
        } finally {
            try {
                if (con != null && !con.isClosed()) {
                    con.close();
                }
            } catch (SQLException e) {
                log.error("Failed to close the connection, {}", e.getMessage());
            }
        }
        return jasperPrint;
    }//fillReport

    public void cleanupExistingReport(String fileJrxml, String reportName) {
        this.cleanupFileOnExists(fileJrxml.replace(".jrxml", ".jasper"));
        this.cleanupFileOnExists(reportName);
    }//cleanupExistingReport

    private void cleanupFileOnExists(String fileName) {
        try {
            log.info("cleanup of file: {}", fileName);
            File reportFile = new File(applicationPropertiesConfiguration.getREPORT_RESOURCE_PATH(), fileName);
            if (reportFile.exists()) {
                reportFile.delete();
                log.info("{} has been deleted", fileName);
            }
        } catch (Exception e) {
            log.error("{} cleanup filed {}", fileName, e.getMessage());
        }
    }//cleanupFileOnExists

    public String getGenFileDir() {
        return applicationPropertiesConfiguration.getREPORT_RESOURCE_PATH().concat("/generated/");
    }//getGenFileDir

    public String getRptFileDir() {
        return applicationPropertiesConfiguration.getREPORT_RESOURCE_PATH().concat("/rptfiles/");
    }//getRptFileDir

    public String getJasperFilePath(String reportJrxml) {
        String jasperName = reportJrxml.replace(".jrxml", ".jasper");
        jasperName = jasperName.replace("/rptfiles/", "/generated/");
//      new FileWriter(new File(jasperName)); 
        return jasperName;
    }//getJasperFilePath

}
