package com.dw.ngms.cis.uam.entity;

import com.dw.ngms.cis.uam.enums.Status;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Data
@Getter
@Setter
@ToString
@NoArgsConstructor
@Table(name = "DOCUMENTSTORE")
public class DocumentStore implements Serializable {

	private static final long serialVersionUID = 7700110758724840308L;
	
	@Id
	@Column(name = "DOCUMENTID")
	@SequenceGenerator(name = "generator", sequenceName = "document_seq", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.IDENTITY, generator = "generator")
	private Long documentId;
	
	@Column(name = "DOCUMENTSTORECODE")
	private String documentStoreCode;
	
	@Column(name = "DOCUMENTPATH")
	private String documentPath;

	@Column(name = "CREATEDDATE")
	private Date createdDate = new Date();
}
