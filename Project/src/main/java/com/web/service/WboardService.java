package com.web.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.web.entity.Wboard;


public interface WboardService {

	List<Wboard> getAllWboards();
	Wboard createWboard(Wboard wboard);
	Wboard updateWboard(String wboardName, Wboard wboardDetails);
	void deleteWboard(String wboardName);
}
