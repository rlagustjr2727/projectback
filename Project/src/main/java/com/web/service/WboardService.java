package com.web.service;

import java.util.List;

import com.web.entity.Wboard;

public interface WboardService {
    List<Wboard> getAllWboards();
    Wboard createWboard(Wboard wboard);
    Wboard updateWboard(String wboardName, Wboard wboardDetails);
    boolean deleteProductByName(String wboardName); // 메서드 이름 수정 및 반환형 boolean으로 변경
    void saveWboard(Wboard wboard);
    Wboard findByWboardName(String wboardName);
//    boolean existsByWboardName(String wboardName); // 중복체크
	List<Wboard> getFilteredWboards(String query, String wboard_type, String wboard_origin, String wboard_abv_type,
			String wboard_yo);
}
