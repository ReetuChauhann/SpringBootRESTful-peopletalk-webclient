package com.reetu.beans;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("/prototype")
public class Message {
      private int pid;
      private String messg,semail,remail,filename;
      private LocalDate cdate;
  	  private String ctime;
  	
  	
  	public Message() {
  		cdate=LocalDate.now();
  		ctime = LocalTime.now().format(DateTimeFormatter.ofPattern("hh:mm a"));
  	}
  	
  	public java.time.LocalDate getCdate() {
  		return cdate;
  	}
	public String getCtime() {
		return ctime;
	}

	public void setCtime(String utime) {
		this.ctime = utime;
	}

	public void setCdate(LocalDate udate) {
		this.cdate = udate;
	}

	public int getPid() {
		return pid;
	}
	public void setPid(int pid) {
		this.pid = pid;
	}
	public String getMessg() {
		return messg;
	}
	public void setMessg(String messg) {
		this.messg = messg;
	}
	public String getSemail() {
		return semail;
	}
	public void setSemail(String semail) {
		this.semail = semail;
	}
	public String getRemail() {
		return remail;
	}
	public void setRemail(String remail) {
		this.remail = remail;
	}
	public String getFilename() {
		return filename;
	}
	public void setFilename(String filename) {
		this.filename = filename;
	}
}
