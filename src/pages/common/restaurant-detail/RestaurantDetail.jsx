import React, { useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import { notification } from "antd";
import Header from "../../../components/header/Header";
import Banner from "../../../components/home-baner/Banner";
import InforRestaurant from "../card/InforRestaurant";
import styles from "./RestaurantDetail.module.css";
import Footer from "../../../components/footer/Footer";
import Explore from "../../../ui/explore/Explore";
import khamphaimg from "../../../assets/Img/loginpage1.jpg";
import ReviewSummary from "../../../ui/review-sumary/ReviewSummary";
import RestaurantMenu from "../restaurant-menu/RestaurantMenu";
import CommentItem from "../comment-item/CommentItem";
import logo from "../../../../src/assets/Img/account.png";
import defaultImage from "../../../../src/assets/Img/loginpage.jpg";
import { getRestaurantById } from "../../../services/restaurantService"; 
import { getCommentsByRestaurantId } from "../../../services/commentService"; 

export default function RestaurantDetail() {
  const { id } = useParams();
  const [restaurant, setRestaurant] = useState(null);
  const [loading, setLoading] = useState(true);
  const [comments, setComments] = useState([]);
  const [page, setPage] = useState(0);
  const [hasMore, setHasMore] = useState(true);
  const [refreshComments, setRefreshComments] = useState(false);
  const size = 3;

  const myRatings = {
    viTri: 7.8,
    giaCa: 8.2,
    chatLuong: 8.2,
    phucVu: 7.6,
    khongGian: 7.4,
    binhLuan: 15,
  };

  const fixImageUrl = (url) => {
    if (url.startsWith("https://images.foody.vn/")) {
      const parts = url.split("/").pop();
      return `https://down-tx-vn.img.susercontent.com/${parts}`;
    }
    return url;
  };

 useEffect(() => {
  const fetchRestaurant = async () => {
    try {
      const data = await getRestaurantById(id);
      if (data && data.id) setRestaurant(data);
    } catch (err) {
      notification.error({
        message: "Thất bại",
        description: `Lỗi tải nhà hàng:", err`
      });
    } finally {
      setLoading(false);
    }
  };

  fetchRestaurant();
}, [id]);

useEffect(() => {
  setComments([]);
  setPage(0);
  setHasMore(true);
}, [id]);

useEffect(() => {
  const loadComments = async () => {
    try {
      const response = await getCommentsByRestaurantId(id, page, size);
      const newComments = response.body || [];

      setComments((prev) => {
        const existingIds = new Set(prev.map((c) => c.id));
        const filtered = newComments.filter((c) => !existingIds.has(c.id));
        return [...prev, ...filtered];
      });

      if (newComments.length < size) setHasMore(false);
    } catch (err) {
      notification.error({
        message: "Thất bại",
        description: `Lỗi tải bình luận:", err`    
      });
    }
  };

  if (id) loadComments();
}, [page, id, refreshComments]);

// Cuộn đến cuối để tăng page
useEffect(() => {
  const handleScroll = () => {
    if (
      window.innerHeight + window.scrollY >= document.body.offsetHeight - 200 &&
      hasMore
    ) {
      setPage((prevPage) => prevPage + 1);
    }
  };

  window.addEventListener("scroll", handleScroll);
  return () => window.removeEventListener("scroll", handleScroll);
}, [hasMore]);
  if (loading) return <p>Đang tải...</p>;
  if (!restaurant) return <p>Không tìm thấy nhà hàng.</p>;

  return (
    <div className = {styles.pageWrapper} style={{ display: "flex", flexDirection: "column" }}>
      <Header />
      <div className={styles.containerDetail}>
        <div className={styles.bannerDetail}>
          <div className={styles.left}>
            <img
              src={fixImageUrl(restaurant.photoUrl)}
              alt="banner-detail"
              width="100%"
              height="300px"
              onError={(e) => {
                e.target.onerror = null;
                e.target.src = defaultImage;
              }}
            />
          </div>
          <div className={styles.right}>
            <InforRestaurant
              breadcrumb={`${restaurant.city} > ${restaurant.district} > ${restaurant.houseNumber}`}
              name={restaurant.name}
              shortDesc="Quán ăn - Món Việt - Gia đình, Nhóm hội, Giao hàng..."
              branchLink="#"
              ratings={myRatings}
              address={restaurant.address}
              time="Đang mở cửa (10:00 - 22:00)"
              price="10.000đ - 1.000.000đ"
              phone={restaurant.phone}
            />
          </div>
        </div>

        <div className={styles.bodyDetail}>
          <div className={styles.stickyExplore}>
            <Explore imageUrl={khamphaimg} width="200px" height="300px" />
          </div>
          <div>
            <RestaurantMenu restaurantId={restaurant.id} />
            {comments.length > 0 ? (
              comments.map((comment, index) => (
                <CommentItem
                  key={index}
                  avatar={logo} 
                  username={comment.customer?.fullName || "Người dùng ẩn danh"}
                  date={comment.createdAt}
                  content={comment.comment}
                  rating={comment.avgRatingText}
                />
              ))
            ) : (
              <p>Chưa có bình luận nào.</p>
            )}
          </div>
          <div className={styles.reviewSumary}>
            <ReviewSummary
              ratings={{
                total: restaurant.totalReviews,
                excellent: 17,
                good: 17,
                average: 3,
                bad: 1,
                criteria: [
                  { label: "Vị trí", value: myRatings.viTri },
                  { label: "Giá cả", value: myRatings.giaCa },
                  { label: "Chất lượng", value: myRatings.chatLuong },
                  { label: "Phục vụ", value: myRatings.phucVu },
                  { label: "Không gian", value: myRatings.khongGian },
                ],
                overall: restaurant.avgRatingText,
              }}
              restaurantId={restaurant.id}
              onCommentSuccess={() => setRefreshComments((prev) => !prev)}
            />
          </div>
        </div>
      </div>
      <Footer />
    </div>
  );
}
