package com.etfbp.dms.models;


import java.util.Set;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.transaction.Transactional;


@Entity
@Table(name = "document", schema = "public")
public class Document {
	
	@Id
	@SequenceGenerator(name="identifier", sequenceName="doc_seq", allocationSize=1)  
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="identifier")
	@Column(name = "id")
    private Integer id; 
	
	@Column(name="user_id", nullable=false)
    private Integer userId;
     
    @Column(name="name", length=100, nullable=false)
    private String name;

	@Column(name="description", length=255)
    private String description;
     
    @Column(name="type", length=100, nullable=false)
    private String type;
     
    @Lob 
    @Basic(fetch = FetchType.LAZY)
    @Column(name="content", nullable=false)
    private byte[] content;
     
	@ManyToMany(targetEntity = User.class, cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinTable(name = "permission_users", joinColumns = @JoinColumn(name="document_id", referencedColumnName = "id"),
							inverseJoinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"))
	private Set<User> users;
     
	@ManyToMany(targetEntity = Grupa.class, cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinTable(name = "permission_groups", joinColumns = @JoinColumn(name="document_id", referencedColumnName = "id"),
							inverseJoinColumns = @JoinColumn(name = "group_id", referencedColumnName = "id"))
	private Set<Grupa> grupe;
	
    public Integer getId() {
        return id;
    }
 
    public void setId(Integer id) {
        this.id = id;
    }
    public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer useriId) {
		this.userId = userId;
	}
 
    public String getName() {
        return name;
    }
 
    public void setName(String name) {
        this.name = name;
    }
 
    public String getDescription() {
        return description;
    }
 
    public void setDescription(String description) {
        this.description = description;
    }
 
    public String getType() {
        return type;
    }
 
    public void setType(String type) {
        this.type = type;
    }
 
    public byte[] getContent() {
        return content;
    }
 
    public void setContent(byte[] content) {
        this.content = content;
    }
    
    
 
    public Set<User> getUsers() {
		return users;
	}


	public void setUsers(Set<User> users) {
		this.users = users;
	}

	
	public Set<Grupa> getGrupe() {
		return grupe;
	}

	public void setGrupe(Set<Grupa> grupe) {
		this.grupe = grupe;
	}

	public Document() {}
    public Document(Integer userId, String name, String description, String type, byte[] content, Set<User> sharedWithUsers,
    		Set<Grupa> sharedWithGroups) {
		super();
		this.userId = userId;
		this.name = name;
		this.description = description;
		this.type = type;
		this.content = content;
		this.users = sharedWithUsers;
		this.grupe = sharedWithGroups;
	}
    
    @Override
    public String toString() {
        return "UserDocument [id=" + id + ", name=" + name + ", description="
                + description + ", type=" + type + "]";
    }
 
 
     
}