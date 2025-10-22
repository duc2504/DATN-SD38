package com.example.datn.Service;

import com.example.datn.DTO.CRUDphukien.DanhMucPhuKienDTO;
import com.example.datn.DTO.DanhMucPhuKien.PhuKienThuocTinhDTO;
import com.example.datn.DTO.DanhMucPhuKien.TrangChuPhuKien;
import com.example.datn.Repository.DanhMucPhuKienRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@AllArgsConstructor
@Service
public class DanhMucPhuKienService {
    private final DanhMucPhuKienRepository danhMucPhuKienRepository;

    public List<DanhMucPhuKienDTO> getAllDanhMucPhuKienWithCount() {
        return danhMucPhuKienRepository.findAllWithCount();
    }

    @PersistenceContext
    private EntityManager entityManager;

    public List<TrangChuPhuKien> getPhuKienByDanhMuc(Integer maDanhMucPhuKien) {
        // ✅ CÂU QUERY ĐÃ SỬA LỖI TYPO
        String sqlQuery = """
        WITH CheapestAccessoryVariant AS (
            SELECT
                btpk.maPhuKien,
                btpk.maSKUPhuKien,
                btpk.gia,
                ROW_NUMBER() OVER(PARTITION BY btpk.maPhuKien ORDER BY btpk.gia ASC, btpk.maSKUPhuKien ASC) as rn
            FROM BienThePhuKien btpk
        ),
        AllAccessoryAttributes AS (
            SELECT DISTINCT
                btpk.maPhuKien,
                ttph.tenThuocTinh,      -- Sửa từ 'ttpk' thành 'ttph'
                ttph.giaTriThuocTinh    -- Sửa từ 'ttpk' thành 'ttph'
            FROM BienThePhuKien btpk
            JOIN ThuocTinhPhuKien ttph ON btpk.maSKUPhuKien = ttph.maSKUPhuKien
        )
        SELECT
            pk.maPhuKien,
            pk.tenPhuKien,
            pk.thuongHieu,
            pk.moTa,
            pk.soLuong,
            dmpk.maDanhMucPhuKien,
            dmpk.tenDanhMucPhuKien,
            cav.maSKUPhuKien,
            cav.gia,
            aaa.tenThuocTinh,
            aaa.giaTriThuocTinh
        FROM PhuKien pk
        JOIN DanhMucPhuKien dmpk ON pk.maDanhMucPhuKien = dmpk.maDanhMucPhuKien
        LEFT JOIN CheapestAccessoryVariant cav ON pk.maPhuKien = cav.maPhuKien AND cav.rn = 1
        LEFT JOIN AllAccessoryAttributes aaa ON pk.maPhuKien = aaa.maPhuKien
        WHERE pk.maDanhMucPhuKien = :maDanhMucPhuKien
        """;

        Query query = entityManager.createNativeQuery(sqlQuery);
        query.setParameter("maDanhMucPhuKien", maDanhMucPhuKien);

        List<Object[]> rows = query.getResultList();

        Map<Integer, TrangChuPhuKien> phuKienMap = new LinkedHashMap<>();
        for (Object[] r : rows) {
            Integer currentMaPhuKien = ((Number) r[0]).intValue();
            TrangChuPhuKien dto = phuKienMap.get(currentMaPhuKien);

            if (dto == null) {
                dto = new TrangChuPhuKien();
                dto.setMaPhuKien(currentMaPhuKien);
                dto.setTenPhuKien((String) r[1]);
                dto.setThuongHieu((String) r[2]);
                dto.setMoTa((String) r[3]);
                dto.setSoLuong(r[4] != null ? ((Number) r[4]).intValue() : null);
                dto.setMaDanhMucPhuKien(r[5] != null ? ((Number) r[5]).intValue() : null);
                dto.setTenDanhMucPhuKien((String) r[6]);
                dto.setMaSKUPhuKien((String) r[7]);
                dto.setGia((BigDecimal) r[8]);
                dto.setChiTietThuocTinh(new ArrayList<>());
                phuKienMap.put(currentMaPhuKien, dto);
            }

            String tenThuocTinh = (String) r[9];
            String giaTriThuocTinh = (String) r[10];
            if (tenThuocTinh != null && giaTriThuocTinh != null) {
                PhuKienThuocTinhDTO thuocTinhDTO = new PhuKienThuocTinhDTO(tenThuocTinh, giaTriThuocTinh);
                if (!dto.getChiTietThuocTinh().contains(thuocTinhDTO)) {
                    dto.getChiTietThuocTinh().add(thuocTinhDTO);
                }
            }
        }
        return new ArrayList<>(phuKienMap.values());
    }
}