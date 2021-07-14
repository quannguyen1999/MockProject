package com.main.entity;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data//: tự tạo get và set
@AllArgsConstructor//: tự tạo constructor có tham số
@NoArgsConstructor//: tự tạo constructor không có tham số
@ToString//: tự tạo to String
@Entity
@Table
public class Post implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int idPost;
	
	@Column(columnDefinition = "nvarchar(100)")
	private String name;
	
	private LocalDate dateCreated;
	
	private LocalDate dateUpdated;
	
	private Boolean status;
	
	@Column(columnDefinition = "nvarchar(max)")
	private String content;
	
	@ManyToOne
	@JoinColumn(name = "userName")
	private Account account;
	
	@ManyToOne
	@JoinColumn(name = "idCategory")
	private Category category;
	
	 // the column parentTree is going to return the object Tree, instead of the parentTree as a Long
	@ManyToOne
    @JoinColumn(name="idPostSelf")  
	private Post post;

	@OneToMany(mappedBy = "post", orphanRemoval = true,cascade = CascadeType.ALL)//(cascade=CascadeType.ALL, orphanRemoval=true,mappedBy = "post")
	private List<Post> listPostSelf = new ArrayList<Post>();
	
	private TypePost typePost;
	
	private String image;

	public Post(int idPost) {
		super();
		this.idPost = idPost;
	}
}
