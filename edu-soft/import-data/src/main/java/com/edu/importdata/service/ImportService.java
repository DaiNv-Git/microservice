package com.edu.importdata.service;

import java.io.IOException;
import java.text.ParseException;

public interface ImportService {

    void initialAll() throws IOException, ParseException;

    void initialUsers() throws IOException;

    void initialRooms() throws IOException;

    void initialGrades() throws IOException;

    void importGrades(String path) throws IOException;

    void initialSubjects() throws IOException, ParseException;

    void initialSubjectMembers() throws IOException;

    void initialExams() throws IOException, ParseException;
}
