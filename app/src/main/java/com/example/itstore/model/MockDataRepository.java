package com.example.itstore.model;

import com.example.itstore.R;

import java.util.ArrayList;
import java.util.List;

public class MockDataRepository {
    private static MockDataRepository instance;
    private List<Product> allProducts;
    private List<Category> allCategories;
    public static MockDataRepository getInstance() {
        if (instance == null) {
            instance = new MockDataRepository();
        }
        return instance;
    }
    private MockDataRepository() {
        allProducts = new ArrayList<>();
        allCategories = new ArrayList<>();
        generateMockData();
    }
    private void generateMockData(){
        String mockImageUrl = "android.resource://com.example.itstore/" + R.drawable.ram1;
        allCategories.add(new Category(-1, "Tất cả", mockImageUrl));
        allCategories.add(new Category(1, "CPU", mockImageUrl));
        allCategories.add(new Category(2, "RAM", mockImageUrl));
        allCategories.add(new Category(3, "VGA", mockImageUrl));
        List<ProductImage> defaultImages = new ArrayList<>();
        defaultImages.add(new ProductImage(1, mockImageUrl, true));
        List<ProductVariant> var1 = new ArrayList<>();
        var1.add(new ProductVariant(1, "Mặc định", 15000000.0, 18000000.0, 50));
        allProducts.add(new Product(101, 1, "CPU Intel Core i9-14900K", "CPU mạnh nhất của Intel, 24 nhân 32 luồng siêu mượt.", var1, defaultImages, 1));
        List<ProductVariant> var2 = new ArrayList<>();
        var2.add(new ProductVariant(2, "32GB", 2500000.0, 2800000.0, 150));
        var2.add(new ProductVariant(6, "64GB", 4800000.0, 5000000.0, 50));
        allProducts.add(new Product(102, 2, "RAM Corsair Vengeance 32GB", "RAM Kingston FURY Beast DDR4 16GB 3200MHz là giải pháp nâng cấp hoàn hảo, mang đến hiệu năng cực đỉnh cho dàn PC của bạn. Thiết kế tản nhiệt nhôm nguyên khối màu đen cá tính không chỉ giúp linh kiện hoạt động mát mẻ, ổn định trong thời gian dài mà còn tôn lên vẻ đẹp góc cạnh, đậm chất gaming cho hệ thống.\n\n" +
                "Được trang bị tính năng Plug N Play tự động ép xung lên tốc độ cao nhất mà hệ thống hỗ trợ, bạn không cần phải vào BIOS tinh chỉnh rườm rà. Sản phẩm tương thích hoàn hảo và đã được kiểm tra nghiêm ngặt trên cả hai nền tảng bo mạch chủ Intel và AMD mới nhất.\n\n" +
                "Ưu điểm nổi bật:\n" +
                "- Dung lượng 16GB dư sức xử lý đa nhiệm, thiết kế đồ họa 2D/3D và chiến mượt các tựa game AAA.\n" +
                "- Tốc độ Bus 3200MHz kết hợp cùng độ trễ thấp CL16 giúp tối ưu thời gian phản hồi, loại bỏ giật lag.\n" +
                "- Hỗ trợ cấu hình Intel XMP 2.0 giúp việc ép xung trở nên dễ dàng chỉ với một cú click chuột.", var2, defaultImages, 2));
        List<ProductVariant> var3 = new ArrayList<>();
        var3.add(new ProductVariant(3, "24GB", 50000000.0, 55000000.0, 10));
        allProducts.add(new Product(103, 3, "VGA NVIDIA RTX 4090", "Card đồ họa khủng nhất hiện nay.", var3, defaultImages, 3));
        List<ProductVariant> var4 = new ArrayList<>();
        var4.add(new ProductVariant(4, "Mặc định", 14000000.0, 18000000.0, 30));
        allProducts.add(new Product(104, 1, "CPU AMD Ryzen 9 7950X", "Đối thủ truyền kiếp của Core i9.", var4, defaultImages, 4));
        List<ProductVariant> var5 = new ArrayList<>();
        var5.add(new ProductVariant(5, "16GB", 1200000.0, 1500000.0, 200));
        allProducts.add(new Product(105, 2, "RAM Kingston Fury 16GB", "RAM DDR4 giá rẻ hiệu năng cao.", var5, defaultImages, 5));
    }
    public List<Product> getAllProducts() {
        return allProducts;
    }

    public List<Category> getAllCategories() {
        return allCategories;
    }
    public void updateProduct(Product updatedProduct) {
        if (updatedProduct == null || allProducts == null) return;
        for (int i = 0; i < allProducts.size(); i++) {
            if (allProducts.get(i).getId() == updatedProduct.getId()) {
                allProducts.set(i, updatedProduct);
                return;
            }
        }
    }

}
