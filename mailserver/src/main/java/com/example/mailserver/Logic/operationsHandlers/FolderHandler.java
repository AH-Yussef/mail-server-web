package com.example.mailserver.Logic.operationsHandlers;

// import com.example.mailserver.Email;
import com.example.mailserver.Logic.JsonEmailConverter;
import com.example.mailserver.Logic.Folders.Folder;
import com.example.mailserver.Logic.Folders.FolderFactory;
import com.example.mailserver.Logic.Folders.FoldersMap;

import javax.imageio.IIOException;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
//change
public class FolderHandler {
    private FilesHandler fileHandler;
    private FoldersMap foldersMap;
    private String userEmail;

    public FolderHandler(FoldersMap foldersMap, String userEmail, FilesHandler filesHandler){
        this.fileHandler = filesHandler;
        this.foldersMap = foldersMap;
        this.userEmail = userEmail;
    }

    private void createSystemFolder(String folderName){
        Folder newSystemFolder = FolderFactory.getInstance().createFolder("system", folderName, userEmail);
        newSystemFolder.setFilesHandler(fileHandler);
        foldersMap.addFolder(folderName, newSystemFolder);
    }

    private void createExistingCustomFolder(String folderName){
        Folder newCustomFolder = FolderFactory.getInstance().createFolder("custom", folderName, userEmail);
        newCustomFolder.setFilesHandler(fileHandler);
        foldersMap.addFolder(folderName, newCustomFolder);
    }

    public void createExistingFolders(){
        //create system folders
        String[] systemFoldersNames = {"inbox", "sent", "draft", "trash"};
        for (String folderName: systemFoldersNames){
            createSystemFolder(folderName);
        }
        //creating existing custom folders
        String[] existingFolderNames = getExistingCustomFolderNames();
        if(existingFolderNames != null){
            for(String folderName: existingFolderNames){
                createExistingCustomFolder(folderName);
            }
        }
    }

    public boolean createNewCustomFolder(String folderName) throws IOException {
        if(foldersMap.hasFolder(folderName)) return false;
        File newCustomFolder = new File(getFolderPath(folderName));
        try{
            newCustomFolder.createNewFile();
        }
        catch (IIOException e){
            e.printStackTrace();
        }

        String[] existingCustomFolderNames = getExistingCustomFolderNames();
        String[] newCustomFoldersNames;
        String foldersFilePath = System.getProperty("user.dir") + "/mailserver/Database/Users/" + userEmail + "/folders.json";;
        if(existingCustomFolderNames == null){
            newCustomFoldersNames = new String[1];
        }else{
            newCustomFoldersNames = Arrays.copyOf(existingCustomFolderNames, existingCustomFolderNames.length+1);
        }
        newCustomFoldersNames[newCustomFoldersNames.length - 1] = folderName;
        fileHandler.writeFileFromArray(foldersFilePath, newCustomFoldersNames);
        return true;
    }

    public boolean deleteFolder(String folderName){
        if(foldersMap.getFolder(folderName).isImmutable()) return false;
        File myFile = new File(getFolderPath(folderName));
        return myFile.delete();
    }

    public boolean renameFolder(String oldFolderName, String newFolderName) throws IOException {
        if(!foldersMap.hasFolder(oldFolderName)) return false;
        Folder oldFolder = foldersMap.getFolder(oldFolderName);
        if(oldFolder == null || oldFolder.isImmutable()) return false;
        if(foldersMap.hasFolder(newFolderName)) return false;

        File oldFile = new File(getFolderPath(oldFolderName));
        File newFile = new File(getFolderPath(newFolderName));

        if (newFile.exists()) throw new java.io.IOException("file exists");
        boolean success =  oldFile.renameTo(newFile);
        if(!success) return false;

        oldFolder.setFileName(newFolderName);

        String[] existingCustomFolderNames = getExistingCustomFolderNames();

        for(int i = 0; i < existingCustomFolderNames.length; i++){
            if(existingCustomFolderNames[i].equals(oldFolderName)){
                existingCustomFolderNames[i] = newFolderName;
                break;
            }
        }
        String foldersFilePath = System.getProperty("user.dir") + "/mailserver/Database/Users/" + userEmail + "/folders.json";;
        fileHandler.writeFileFromArray(foldersFilePath, existingCustomFolderNames);
        return true;
    }

    private String getFolderPath(String folderName){
        return System.getProperty("user.dir") + "/mailserver/Database/Users/" + userEmail + "/" + folderName + ".json";
    }

    public String[] getExistingCustomFolderNames(){
        String existingCustomFoldersPath = System.getProperty("user.dir") + "/mailserver/Database/Users/" + userEmail + "/folders.json";
        String existingCustomFoldersNamesStr = fileHandler.readFile(existingCustomFoldersPath);
        return JsonEmailConverter.getInstance().jsonArrayToStringArray(existingCustomFoldersNamesStr);
    }
}