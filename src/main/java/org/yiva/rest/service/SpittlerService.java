package org.yiva.rest.service;

import org.springframework.stereotype.Service;

/**
 * @author yiva
 *
 */
@Service
public class SpittlerService {
	
	private long id;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}
	
	public long getSpittleById(long id){
		return id;
	}

}
