package com.dw.ngms.cis.exception;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class RestResponse
{
    private String message;

    private long timestamp;

    private String error;

    private int status;

    private String path;
    
    private String exception;

    public String getMessage ()
    {
        return message;
    }

    public void setMessage (String message)
    {
        this.message = message;
    }

    public long getTimestamp ()
    {
        return timestamp;
    }

    public void setTimestamp (long timestamp)
    {
        this.timestamp = timestamp;
    }

    public String getError ()
    {
        return error;
    }

    public void setError (String error)
    {
        this.error = error;
    }

    public int getStatus ()
    {
        return status;
    }

    public void setStatus (int status)
    {
        this.status = status;
    }

    public String getPath ()
    {
        return path;
    }

    public void setPath (String path)
    {
        this.path = path;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [message = "+message+", timestamp = "+timestamp+", error = "+error+", status = "+status+", path = "+path+"]";
    }

	public String getException() {
		return exception;
	}

	public void setException(String exception) {
		this.exception = exception;
	}

    public RestResponse() {
    }
}