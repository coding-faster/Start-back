package com.example.dao;

import java.util.List;

import com.example.domain.BBSVO;

public interface BBSDAO {
	public List<BBSVO> list();
	public BBSVO read(int bid);
	public void insert(BBSVO vo);
	public void update(BBSVO vo);
	public void delete(int bid);
}
