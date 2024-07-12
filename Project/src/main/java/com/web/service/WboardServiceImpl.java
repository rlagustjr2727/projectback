package com.web.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.web.entity.Wboard;
import com.web.repository.WboardRepository;

@Service
public class WboardServiceImpl implements WboardService{

	@Autowired
	private WboardRepository wboardRepository;

	@Override
	public List<Wboard> getAllWboards() {
		return wboardRepository.findAll();
	}

	@Override
	public Wboard createWboard(Wboard wboard) {
		return wboardRepository.save(wboard);
	}

	  @Override
	    public Wboard updateWboard(String wboardName, Wboard wboardDetails) {
	        Wboard wboard = wboardRepository.findById(wboardName)
	                .orElseThrow(() -> new RuntimeException("Wboard not found with name: " + wboardName));

	        wboard.setWboardImg(wboardDetails.getWboardImg());
	        wboard.setWboardInfo(wboardDetails.getWboardInfo());
	        wboard.setWboardTip(wboardDetails.getWboardTip());
	        wboard.setWboardType(wboardDetails.getWboardType());
	        wboard.setWboardOrigin(wboardDetails.getWboardOrigin());
	        wboard.setWboardAlcohol(wboardDetails.getWboardAlcohol());
	        wboard.setWboardAge(wboardDetails.getWboardAge());
	        wboard.setWboardAlcoholType(wboardDetails.getWboardAlcoholType());

	        return wboardRepository.save(wboard);
	    }

	@Override
	public void deleteWboard(String wboardName) {
	     Wboard wboard = wboardRepository.findById(wboardName)
	                .orElseThrow(() -> new RuntimeException("Wboard not found with name: " + wboardName));

	        wboardRepository.delete(wboard);
		
	}

	@Override
	public void saveWboard(Wboard wboard) {
		 wboardRepository.save(wboard);
	}

	@Override
	public Wboard findByWboardName(String wboardName) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean deleteProductByName(String wboard_name) {
		// TODO Auto-generated method stub
		return false;
	}

	
	
}







