package com.dw.ngms.cis.uam.controller;

import com.dw.ngms.cis.controller.MessageController;
import com.dw.ngms.cis.im.entity.Requests;
import com.dw.ngms.cis.uam.configuration.ApplicationPropertiesConfiguration;
import com.dw.ngms.cis.uam.dto.FilePathsDTO;
import com.dw.ngms.cis.uam.entity.DocumentStore;
import com.dw.ngms.cis.uam.jsonresponse.UserControllerResponse;
import com.dw.ngms.cis.uam.service.DocumentStoreService;
import com.dw.ngms.cis.uam.storage.StorageService;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import static org.springframework.util.StringUtils.isEmpty;

/**
 * Created by swaroop on 2019/06/11.
 */
@RestController
@RequestMapping("/cisorigin.uam/api/v1")
@CrossOrigin(origins = "*")
public class DocumentStoreController extends MessageController {

    @Autowired
    private DocumentStoreService documentStoreService;

    @Autowired
    private StorageService testService;

    @Autowired
    private ApplicationPropertiesConfiguration applicationPropertiesConfiguration;


    @PostMapping("/uploadDocumentFile")
    public ResponseEntity<?> uploadDocumentFile(HttpServletRequest request, @RequestParam MultipartFile[] multipleFiles,
                                                @RequestParam("documentStoreCode") String documentStoreCode
    ) {
        try {
            String message = "";
            String json = null;
            Gson gson = new Gson();
            UserControllerResponse userControllerResponse = new UserControllerResponse();
            Long documentId = this.documentStoreService.getDocumentID();
            DocumentStore documentStore = new DocumentStore();
            documentStore.setDocumentId(documentId);
            if (documentStoreCode == null || StringUtils.isEmpty(documentStoreCode)) {
                documentStore.setDocumentStoreCode("DSC" + Long.toString(documentStore.getDocumentId()));
            } else {
                documentStore = this.documentStoreService.getDocumentByID(documentStoreCode);
                documentStore.setDocumentStoreCode(documentStoreCode);

            }


            if (documentStore.getDocumentPath() != null) {
                List<String> filesExist = new ArrayList<>();
                String pathFromDB = documentStore.getDocumentPath();
                FilePathsDTO filePath = gson.fromJson(pathFromDB, FilePathsDTO.class);
                System.out.println("filePath from uploadDocumentFile " + filePath.getFiles().toString());
                for (String str1 : filePath.getFiles()) {
                    filesExist.add(str1);
                }
                for (MultipartFile f : multipleFiles) {
                    List<String> files = new ArrayList<String>();
                    String fileName = testService.store(f);
                    files.add(f.getOriginalFilename());
                    filesExist.add(applicationPropertiesConfiguration.getUploadDirectoryPath() + fileName);
                    userControllerResponse.setFiles(filesExist);
                    json = gson.toJson(userControllerResponse);
                    documentStore.setDocumentPath(json);
                    this.documentStoreService.saveDocument(documentStore);
                }
            } else {
                List<String> filesExist = new ArrayList<>();
                for (MultipartFile f : multipleFiles) {
                    List<String> files = new ArrayList<String>();
                    String fileName = testService.store(f);
                    files.add(f.getOriginalFilename());
                    filesExist.add(applicationPropertiesConfiguration.getUploadDirectoryPath() + fileName);
                    userControllerResponse.setFiles(filesExist);
                    json = gson.toJson(userControllerResponse);
                    documentStore.setDocumentPath(json);
                    this.documentStoreService.saveDocument(documentStore);
                }
            }
            return ResponseEntity.status(HttpStatus.OK).body(documentStore);
        } catch (Exception exception) {
            return generateFailureResponse(request, exception);
        }
    }   //uploadDocumentFile


    @RequestMapping(value = "/deleteDocument", method = RequestMethod.POST)
    public ResponseEntity<?> deleteDocument(HttpServletRequest request,
                                            @RequestBody @Valid DocumentStore requestsBody,
                                            @RequestParam String documentPath) throws IOException {
        try {
            String json = null;
            Gson gson = new Gson();
            UserControllerResponse userControllerResponse = new UserControllerResponse();
            DocumentStore documentStore = this.documentStoreService.getDocumentByID(requestsBody.getDocumentStoreCode());
            if (isEmpty(documentStore)) {
                return generateEmptyResponse(request, "Documents are  not found with document store code: " + requestsBody.getDocumentStoreCode());
            }
            if (!isEmpty(documentStore)) {
                String pathFromDB = documentStore.getDocumentPath();

                FilePathsDTO filePath = gson.fromJson(pathFromDB, FilePathsDTO.class);
                List<String> fileDeleted = new ArrayList<>();
                for (String str1 : filePath.getFiles()) {
                    fileDeleted.add(str1);
                }
                System.out.println("size is " + fileDeleted.size());
                if (fileDeleted.contains(documentPath)) {
                    Path fileToDeletePath = Paths.get(documentPath);
                    Files.delete(fileToDeletePath);
                    fileDeleted.remove(documentPath);
                    if (fileDeleted.size() == 0) {
                        System.out.println("Size of file is " + fileDeleted);
                        documentStore.setDocumentPath("");
                        this.documentStoreService.saveDocument(documentStore);
                        return ResponseEntity.status(HttpStatus.OK).body("Document Deleted Successfully");
                    }

                }
                userControllerResponse.setFiles(fileDeleted);
                json = gson.toJson(userControllerResponse);
                documentStore.setDocumentPath(json);
                this.documentStoreService.saveDocument(documentStore);
                System.out.println("size is after " + fileDeleted.size());
                return ResponseEntity.status(HttpStatus.OK).body("Document Deleted Successfully");
            }
            return generateEmptyResponse(request, "Document's are  not found");
        } catch (Exception exception) {
            return generateFailureResponse(request, exception);
        }
    }//deleteDocument


    @RequestMapping(value = "/getDocumentList", method = RequestMethod.GET)
    public ResponseEntity<?> getDocumentList(HttpServletRequest request,
                                                 @RequestParam String documentStoreCode) throws IOException {
        try {
            String json = null;
            List<String> test = new ArrayList<>();
            DocumentStore documentStore = this.documentStoreService.getDocumentByID(documentStoreCode);
            if (!isEmpty(documentStore)) {
                String pathFromDB = documentStore.getDocumentPath();
                Gson gson = new Gson();
                FilePathsDTO filePath = gson.fromJson(pathFromDB, FilePathsDTO.class);
                System.out.println("filePath of the document is " + filePath.getFiles().toString());
                List<String> files = new ArrayList<String>();
                for (String str1 : filePath.getFiles()) {
                    files.add(str1);
                    json = gson.toJson(files);

                }
                return ResponseEntity.status(HttpStatus.OK).body(json);
            } else {
                return ResponseEntity.status(HttpStatus.OK).body(test);
            }

        } catch (Exception exception) {
            return generateFailureResponse(request, exception);
        }
    }//getDocumentList



    @RequestMapping(value = "/downloadDocuments", method = RequestMethod.POST)
    public ResponseEntity<InputStreamResource> downloadDocuments(HttpServletRequest request,
                                                                     @RequestBody @Valid DocumentStore requests) throws IOException {
        DocumentStore documentStore = this.documentStoreService.getDocumentByID(requests.getDocumentStoreCode());
        System.out.println("documentStore documents are one " + documentStore.getDocumentPath());
        String pathFromDB = documentStore.getDocumentPath();

        Gson gson = new Gson();
        FilePathsDTO filePath = gson.fromJson(pathFromDB, FilePathsDTO.class);
        System.out.println("filePath is " + filePath.getFiles().toString());
        List<String> files = new ArrayList<String>();
        for (String str1 : filePath.getFiles()) {
            System.out.println(str1);
            files.add(str1);
            zipFiles(files);
        }
        File file = new File(applicationPropertiesConfiguration.getDownloadDirectoryPath() + "DocumentsDownloadFiles.zip");

        InputStreamResource resource = new InputStreamResource(new FileInputStream(file));
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename= DocumentsDownloadFiles.zip")
                .contentType(MediaType.parseMediaType("application/octet-stream"))
                .contentLength(file.length()) //
                .body(resource);
    }//downloadDispatchDocuments



    public void zipFiles(List<String> files) {

        FileOutputStream fos = null;
        ZipOutputStream zipOut = null;
        FileInputStream fis = null;
        try {
            fos = new FileOutputStream(applicationPropertiesConfiguration.getDownloadDirectoryPath() + "DocumentsDownloadFiles.zip");
            zipOut = new ZipOutputStream(new BufferedOutputStream(fos));
            for (String filePath : files) {
                File input = new File(filePath);
                fis = new FileInputStream(input);
                ZipEntry ze = new ZipEntry(input.getName());
                zipOut.putNextEntry(ze);
                byte[] tmp = new byte[4 * 1024];
                int size = 0;
                while ((size = fis.read(tmp)) != -1) {
                    zipOut.write(tmp, 0, size);
                }
                zipOut.flush();
                fis.close();
            }
            zipOut.close();
            System.out.println("Done... Zipped the files...");
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            try {
                if (fos != null) fos.close();
            } catch (Exception ex) {

            }
        }
    }//zipFiles



}
