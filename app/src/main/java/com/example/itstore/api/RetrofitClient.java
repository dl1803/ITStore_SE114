package com.example.itstore.api;

import android.content.Context;

import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

import com.example.itstore.model.AddressRequest;
import com.example.itstore.model.AddressResponse;
import com.example.itstore.model.AuthMessageResponse;
import com.example.itstore.model.BannerResponse;
import com.example.itstore.model.BrandResponse;
import com.example.itstore.model.CancelOrderRequest;
import com.example.itstore.model.CartResponse;
import com.example.itstore.model.CategoryResponse;
import com.example.itstore.model.ChangePasswordRequest;
import com.example.itstore.model.ChangePasswordResponse;
import com.example.itstore.model.CouponResponse;
import com.example.itstore.model.CreateOrderRequest;
import com.example.itstore.model.ForgotPasswordRequest;
import com.example.itstore.model.ForgotPasswordResponse;
import com.example.itstore.model.GoogleLoginRequest;
import com.example.itstore.model.LoginRequest;
import com.example.itstore.model.LoginResponse;
import com.example.itstore.model.LogoutRequest;
import com.example.itstore.model.LogoutResponse;
import com.example.itstore.model.CreateOrderResponse;
import com.example.itstore.model.MarkAsReadRequest;
import com.example.itstore.model.NotificationListResponse;
import com.example.itstore.model.NotificationResponse;
import com.example.itstore.model.NotificationUnreadCountResponse;
import com.example.itstore.model.OrderHistoryResponse;
import com.example.itstore.model.PayOsPaymentResponse;
import com.example.itstore.model.ProductResponse;
import com.example.itstore.model.ProductReviewsResponse;
import com.example.itstore.model.ProfileResponse;
import com.example.itstore.model.RefreshTokenRequest;
import com.example.itstore.model.RefreshTokenResponse;
import com.example.itstore.model.RegisterRequest;
import com.example.itstore.model.RegisterResponse;
import com.example.itstore.model.ResendOtpRequest;
import com.example.itstore.model.ResetPasswordRequest;
import com.example.itstore.model.ResetPasswordResponse;
import com.example.itstore.model.ShipmentFeeRequest;
import com.example.itstore.model.ShipmentFeeResponse;
import com.example.itstore.model.SingleAddressResponse;
import com.example.itstore.model.SingleBrandResponse;
import com.example.itstore.model.SingleCategoryResponse;
import com.example.itstore.model.SingleOrderResponse;
import com.example.itstore.model.SingleProductResponse;
import com.example.itstore.model.TokenRegistrationRequest;
import com.example.itstore.model.UnreviewedResponse;
import com.example.itstore.model.UpdateProfileRequest;
import com.example.itstore.model.ReturnRequestResponse;
import okhttp3.RequestBody;
import java.util.List;
import com.example.itstore.model.ReturnRequestListResponse;
import com.example.itstore.model.ReturnRequestDetailResponse;
import com.example.itstore.model.AddCartItemRequest;
import com.example.itstore.model.UpdateCartItemRequest;
import com.example.itstore.model.VariantReviewsResponse;
import com.example.itstore.model.VerifyOtpRequest;
import com.example.itstore.model.VerifyResetOtpRequest;
import com.example.itstore.model.VerifyResetOtpResponse;
import com.example.itstore.model.WishlistResponse;
import com.example.itstore.model.WishlistMessageResponse;
import com.example.itstore.model.AddWishlistRequest;
import com.example.itstore.model.ChatRequest;
import com.example.itstore.model.ChatResponse;


public class RetrofitClient {
    private static final String BASE_URL = "http://10.0.2.2:3000/";
    private static Retrofit retrofit = null;

    public static ApiService getApiService(Context context) {
        if (retrofit == null) {

            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(new AuthInterceptor(context))
                    .authenticator(new TokenAuthenticator(context))
                    .build();

            retrofit = new Retrofit.Builder().baseUrl(BASE_URL).client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit.create(ApiService.class);
    }

    public interface ApiService {
        @POST("api/auth/login")
        Call<LoginResponse> login(@Body LoginRequest request);

        @POST("api/auth/register")
        Call<RegisterResponse> register(@Body RegisterRequest request);

        @POST("api/auth/forgot-password")
        Call<ForgotPasswordResponse> forgotPassword(@Body ForgotPasswordRequest request);

        @POST("api/auth/verify-reset-password-code")
        Call<VerifyResetOtpResponse> verifyResetPasswordOtp(@Body VerifyResetOtpRequest request);

        @POST("api/auth/reset-password")
        Call<AuthMessageResponse> resetPassword(@Query("token") String token, @Body ResetPasswordRequest request);

        @POST("api/auth/logout")
        Call<LogoutResponse> logout(@Body LogoutRequest request);

        @POST("api/auth/refresh")
        Call<RefreshTokenResponse> refreshToken(@Body RefreshTokenRequest request);

        @POST("api/auth/google")
        Call<LoginResponse> googleLogin(@Body GoogleLoginRequest request);

        @GET("api/users/me")
        Call<ProfileResponse> getProfile();

        @PUT("api/users/me")
        Call<ProfileResponse> updateProfile(@Body UpdateProfileRequest request);

        @PUT("api/users/me/password")
        Call<ChangePasswordResponse> changePassword(@Body ChangePasswordRequest request);

        @Multipart
        @PATCH("api/users/me/avatar")
        Call<Void> updateAvatar(@Part MultipartBody.Part avatar);

        @GET("api/users/me/addresses")
        Call<AddressResponse> getAddresses();

        @POST("api/users/me/addresses")
        Call<SingleAddressResponse> addAddress(@Body AddressRequest request);

        @PUT("api/users/me/addresses/{id}")
        Call<SingleAddressResponse> updateAddress(@Path("id") int id, @Body AddressRequest request);

        @PATCH("api/users/me/addresses/{id}/default")
        Call<SingleAddressResponse> setDefaultAddress(@Path("id") int id);

        @DELETE("api/users/me/addresses/{id}")
        Call<SingleAddressResponse> deleteAddress(@Path("id") int id);
        @GET("api/brands")
        Call<BrandResponse> getBrands();

        @GET("api/coupons")
        Call<CouponResponse> getCoupons();

        @GET("api/cart")
        Call<CartResponse> getCart();
        @POST("api/cart/items")
        Call<CartResponse> addCartItem(@Body AddCartItemRequest request);

        @PUT("api/cart/items/{variant_id}")
        Call<CartResponse> updateCartItem(@Path("variant_id") int variantId, @Body UpdateCartItemRequest request);

        @DELETE("api/cart/items/{variant_id}")
        Call<CartResponse> removeCartItem(@Path("variant_id") int variantId);

        @POST("api/orders")
        Call<CreateOrderResponse> createOrder(@Body CreateOrderRequest request);

        @GET("api/orders")
        Call<OrderHistoryResponse> getOrderHistory();

        @GET("api/orders/{id}")
        Call<SingleOrderResponse> getOrderById(@Path("id") int id);

        @PATCH("api/orders/{id}/cancel")
        Call<SingleOrderResponse> cancelOrder(@Path("id") int id, @Body CancelOrderRequest request);

        @PATCH("api/orders/{id}/confirm-received")
        Call<SingleOrderResponse> confirmReceived(@Path("id") int id);

        @GET("api/brands/{id}")
        Call<SingleBrandResponse> getBrandById(@Path("id") int id);


        @GET("api/categories")
        Call<CategoryResponse> getCategories();
        @GET("api/categories/{id}")
        Call<SingleCategoryResponse> getCategoryById(@Path("id") int id);
        @GET("api/products")
        Call<ProductResponse> getProducts(
                @Query("page") Integer page,
                @Query("limit") Integer limit,
                @Query("keyword") String keyword,
                @Query("category_id") Integer categoryId,
                @Query("brand_id") Integer brandId,
                @Query("price_min") Double priceMin,
                @Query("price_max") Double priceMax,
                @Query("sort") String sort
        );
        @GET("api/products/by-category/{category_id}")
        Call<ProductResponse> getProductsByCategoryBlock(
                @Path("category_id") int categoryId,
                @Query("limit") Integer limit
        );
        @GET("api/products/{id}")
        Call<SingleProductResponse> getProductById(@Path("id") int productId);
        @GET("api/products/{slug}")
        Call<SingleProductResponse> getProductBySlug(@Path("slug") String slug);

        @Multipart
        @POST("api/return-requests")
        Call<ReturnRequestResponse> createReturnRequest(
                @Part("order_id") RequestBody orderId,
                @Part("reason") RequestBody reason,
                @Part("items") RequestBody items,
                @Part List<MultipartBody.Part> images
        );

        @GET("api/return-requests")
        Call<ReturnRequestListResponse> getMyReturnRequests();

        @GET("api/return-requests/{id}")
        Call<ReturnRequestDetailResponse> getMyReturnRequestDetail(@Path("id") int id);

        @POST("api/payments/orders/{orderId}")
        Call<PayOsPaymentResponse> createPayOsPaymentLink(@Path("orderId") int orderId);

        @POST("api/orders/shipment-fee")
        Call<ShipmentFeeResponse> calculateShippingFee(@Body ShipmentFeeRequest request);

        @POST("api/auth/verify-email")
        Call<AuthMessageResponse> verifyEmailOtp(@Body VerifyOtpRequest request);

        @POST("api/auth/resend-verify-email")
        Call<AuthMessageResponse> resendVerifyEmailOtp(@Body ResendOtpRequest request);

        @GET("api/wishlist")
        Call<WishlistResponse> getWishlist();

        @POST("api/wishlist")
        Call<WishlistMessageResponse> addToWishlist(@Body AddWishlistRequest request);

        @DELETE("api/wishlist/{product_id}")
        Call<WishlistMessageResponse> removeFromWishlist(@Path("product_id") int productId);

        @POST("api/chatbot/chat")
        Call<ChatResponse> chat(@Body ChatRequest request);
        @POST("api/notifications/token-registration")
        Call<NotificationResponse> registerFCMToken(@Body TokenRegistrationRequest request);
        @GET("api/notifications")
        Call<NotificationListResponse> getNotifications(
                @Query("page") Integer page,
                @Query("limit") Integer limit,
                @Query("type") String type
        );
        @GET("api/notifications/unread-count")
        Call<NotificationUnreadCountResponse> getUnreadNotificationCount();
        @POST("api/notifications/mark-as-read")
        Call<NotificationResponse> markAsRead(@Body MarkAsReadRequest request);
        @POST("api/notifications/mark-all-as-read")
        Call<NotificationResponse> markAllAsRead();

        @GET("api/banners")
        Call<BannerResponse> getBanners(@Query("sort") String sort,@Query("is_active") Boolean isActive);
        @Multipart
        @POST("api/review")
        Call<okhttp3.ResponseBody> createReview(
                @Part("order_item_id") RequestBody orderItemId,
                @Part("rating") RequestBody rating,
                @Part("comment") RequestBody comment,
                @Part List<okhttp3.MultipartBody.Part> images
        );

        @Multipart
        @PUT("api/review/{review_id}")
        Call<okhttp3.ResponseBody> updateReview(
                @Path("review_id") int reviewId,
                @Part("rating") RequestBody rating,
                @Part("comment") RequestBody comment,
                @Part("deleted_images") RequestBody deletedImages,
                @Part List<okhttp3.MultipartBody.Part> images
        );
        @GET("api/review/unreviewed")
        Call<UnreviewedResponse> getUnreviewedItems();
        @GET("api/review/products/{product_id}")
        Call<ProductReviewsResponse> getProductReviews(
                @Path("product_id") int productId,
                @Query("page") int page,
                @Query("limit") int limit
        );
        @GET("api/review/variants/{variant_id}")
        Call<VariantReviewsResponse> getVariantReviews(
                @Path("variant_id") int variantId,
                @Query("page") int page,
                @Query("limit") int limit
        );
    }
}