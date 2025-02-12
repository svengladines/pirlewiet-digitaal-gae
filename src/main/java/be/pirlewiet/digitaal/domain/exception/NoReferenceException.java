package be.pirlewiet.digitaal.domain.exception;

import be.occam.utils.spring.web.ErrorCode;

public class NoReferenceException extends  PirlewietException{
    public NoReferenceException() {
        super(ErrorCodes.REFERENCED_NO_CODE);
    }
}
