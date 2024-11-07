package be.pirlewiet.digitaal.web.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Map;

public class Controller {

    public static <T> ResponseEntity<T> response(T t, HttpStatus status, Map<String, String> headers ) {

        HttpHeaders headrs = new HttpHeaders();

        //headrs.add("Access-Control-Allow-Origin", "*" ) ;
        //headrs.add("Access-Control-Allow-Methods", "GET,POST,PUT,DELETE,OPTIONS" );
        //headrs.add("Access-Control-Allow-Headers", "Content-Type");
        //headrs.add("Access-Control-Allow-Credentials","true");

        if ( headers != null ) {

            for ( String key : headers.keySet() ) {
                headrs.add( key, headers.get( key ) );
            }

        }

        ResponseEntity<T> response;

        if ( t != null ) {
            response
                    = new ResponseEntity<T>( t, headrs, status );
        }
        else {
            response
                    = new ResponseEntity<T>( headrs, status );
        }

        return response;

    }

    public static <T> ResponseEntity<T> response( T t, HttpStatus status ) {

        return response( t, status, null );

    }

    public static <T> ResponseEntity<T> response( HttpStatus status ) {

        return response( null, status );

    }

}
