package com.example.mailserver.Logic;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.Queue;

import com.example.mailserver.Logic.Folders.TrashFolder;
import com.example.mailserver.Logic.Proxy.SignIn;
import com.example.mailserver.Logic.Proxy.SignUp;

public class Test {
    public static void main(String args[]) {
        // Queue<String> receivers = new LinkedList<String>();
        // Queue<String> attachments = new LinkedList<String>();
        // receivers.add("testREC@g");

        User user = new User("shosh@gmail.com");
        TrashFolder trashFolder = new TrashFolder("trash", true, "shosh@gmail.com");
        trashFolder.deleteForever("713efe52-3540-434d-9d4c-57020591c505");
        

    }
}