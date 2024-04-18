package edu.tcu.cs.peerevalutationtool.system.exception;

import edu.tcu.cs.peerevalutationtool.section.SectionNotFoundByIdAndYearException;
import edu.tcu.cs.peerevalutationtool.section.SectionNotFoundByYearException;
import edu.tcu.cs.peerevalutationtool.section.SectionNotFoundException;
import edu.tcu.cs.peerevalutationtool.system.Result;
import edu.tcu.cs.peerevalutationtool.system.StatusCode;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionHandlerAdvice {

    @ExceptionHandler(SectionNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    Result handleSectionNotFoundException(SectionNotFoundException ex){
        return new Result(false, StatusCode.NOT_FOUND, ex.getMessage());
    }

    @ExceptionHandler(SectionNotFoundByYearException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    Result handleSectionNotFoundException(SectionNotFoundByYearException ex){
        return new Result(false, StatusCode.NOT_FOUND, ex.getMessage());
    }

    @ExceptionHandler(SectionNotFoundByIdAndYearException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    Result handleSectionNotFoundException(SectionNotFoundByIdAndYearException ex){
        return new Result(false, StatusCode.NOT_FOUND, ex.getMessage());
    }

}
