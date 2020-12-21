package com.example.mailserver.TheLOL.Folders;

import com.example.mailserver.TheLOL.Folders.Folder;

import java.util.HashMap;

public class FoldersMap {
    private HashMap<String, Folder> folderMap;

    public FoldersMap(){
        folderMap = new HashMap<>();
    }

    public void setFolderMap(HashMap<String, Folder> folderMap){
        this.folderMap = folderMap;
    }

    public Folder getFolder(String folderKey){
        return folderMap.get(folderKey);
    }

    public Folder deleteFolder(String folderKey){
        return folderMap.remove(folderKey);
    }

    public void addFolder(String folderKey, Folder folder){
        this.folderMap.put(folderKey, folder);
    }

    public boolean hasFolder(String folderKey) {
        return this.folderMap.containsKey(folderKey);
    }
}