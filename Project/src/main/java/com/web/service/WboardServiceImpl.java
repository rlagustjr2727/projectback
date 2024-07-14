package com.web.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.web.entity.Wboard;
import com.web.repository.WboardRepository;

@Service
public class WboardServiceImpl implements WboardService {

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
		wboard.setWboardAbv(wboardDetails.getWboardAbv());
		wboard.setWboardYo(wboardDetails.getWboardYo());
		wboard.setWboardAbvType(wboardDetails.getWboardAbvType());

		return wboardRepository.save(wboard);
	}

	@Override
	public boolean deleteProductByName(String wboardName) {
		Wboard wboard = wboardRepository.findById(wboardName)
				.orElseThrow(() -> new RuntimeException("Wboard not found with name: " + wboardName));
		if (wboard != null) {
			wboardRepository.delete(wboard);
			return true;
		}
		return false;
	}

	@Override
	public void saveWboard(Wboard wboard) {
		wboardRepository.save(wboard);
	}

	@Override
	public Wboard findByWboardName(String wboardName) {
		return wboardRepository.findById(wboardName)
				.orElseThrow(() -> new RuntimeException("Wboard not found with name: " + wboardName));
	}

	// 중복 체크
//	    @Override
//	    public boolean existsByWboardName(String wboardName) {
//	        return wboardRepository.existsByWboardName(wboardName);
//	    }	
	
	public List<Wboard> getFilteredWboards(String query, String wboard_of_type, String wboard_origin, String wboard_abv_type, String wboard_yo) {
	    List<Wboard> wboards = getAllWboards();

	    if (query != null && !query.isEmpty()) {
	        wboards = wboards.stream()
	                .filter(w -> w.getWboardName().toLowerCase().contains(query.toLowerCase()))
	                .collect(Collectors.toList());
	    }

	    if (wboard_of_type != null && !wboard_of_type.isEmpty()) {
	        wboards = wboards.stream()
	                .filter(w -> w.getWboardType().toLowerCase().contains(wboard_of_type.toLowerCase()))
	                .collect(Collectors.toList());
	    }

	    if (wboard_origin != null && !wboard_origin.isEmpty()) {
	        wboards = wboards.stream()
	                .filter(w -> w.getWboardOrigin().equals(wboard_origin))
	                .collect(Collectors.toList());
	    }

	    if (wboard_abv_type != null && !wboard_abv_type.isEmpty()) {
	        wboards = wboards.stream()
	                .filter(w -> w.getWboardAbvType().equals(wboard_abv_type))
	                .collect(Collectors.toList());
	    }

	    if (wboard_yo != null && !wboard_yo.isEmpty()) {
	        wboards = wboards.stream()
	                .filter(w -> w.getWboardYo().equals(wboard_yo))
	                .collect(Collectors.toList());
	    }

	    return wboards;
	}




}
