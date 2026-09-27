/*
 * Product image resolver
 *
 * The backend's imageUrl is still supported, but the storefront uses a
 * product-name map first. This keeps the UI independent of image URLs that
 * may expire, block hot-linking, or be unavailable on the user's network.
 */

const productImages = {
  "Lenovo IdeaPad Slim 5":
    "https://s13emagst.akamaized.net/products/66404/66403887/images/res_72c4b8630da8a157fe9d728c460a5c4a.png",
  "Logitech Wireless Mouse":
    "https://lntsufin.com/storage/mediafiles/catalog/live/15919-525/original/15919-525_image_0.jpg",
  "Sony WH-1000XM5":
    "https://www.jbhifi.com.au/cdn/shop/products/592550-Product-0-I-637878553982254808_1024x1024.jpg",
  "Java Programming Masterclass":
    "https://rezised-images.knhbt.cz/1920x1920/81092576.jpg",
  "Samsung Galaxy A55 5G":
    "https://www.clevercel.co/cdn/shop/files/SamsungGalaxyA555G2024BLACK.webp?crop=center&height=1200&v=1781639745&width=1200",
  "Clean Code":
    "https://www.karankumar.com/content/images/size/w1200/2020/08/clean-code-1.jpg",
  "Spring Boot in Action":
    "https://img.perlego.com/book-covers/2682685/9781638353584.jpg",
  "Designing Data-Intensive Applications":
    "https://miro.medium.com/v2/1%2Anf4kkI-MuoVBi5ZJLo77qA.jpeg",
  "Premium Cotton T-Shirt":
    "https://saintstudio.jetassets.com.br/produto/multifotos/hd/20251031215142_6817993183_DMZ.jpg",
  "Classic Casual Hoodie":
    "https://static.wixstatic.com/media/efd427_91497270318b475699e6668ecf0b3536~mv2.jpg/v1/fill/w_980,h_1117,al_c,q_85,usm_0.66_1.00_0.01,enc_avif,quality_auto/efd427_91497270318b475699e6668ecf0b3536~mv2.jpg",
  "Slim Fit Blue Jeans":
    "https://www.sportsdirect.com/images/imgzoom/64/64404318_xxl.jpg",
  "Lightweight Casual Jacket":
    "https://cdn11.bigcommerce.com/s-n50432g33v/images/stencil/1280x1280/products/8449/18757/HL6400JFZ-SAND_1__24385.1765302489.jpg?c=2",
  "Electric Kettle 1.5L":
    "https://img.drz.lazcdn.com/static/lk/p/770d62d9e69c5db672091577f1cd474b.jpg_720x720q80.jpg",
  "Non-Stick Cookware Set":
    "https://www.cookwarebrands.com.au/cdn/shop/files/CIRCULON_SCRATCHDEFENSE_818390_9PIECECOOKWARESET_1.jpg?v=1730338871&width=1066",
  "Bamboo Storage Organizer":
    "https://images.tcdn.com.br/img/img_prod/1246089/caixa_organizadora_em_bambu_com_3_divisrias_5l_1_20260603082450_9130e19b2564.jpg",
  "Stainless Steel Water Bottle":
    "https://fashionpyramid.co/cdn/shop/files/53a5bfc8c3f83b7863cc7e9800976150.jpg?v=1730559762&width=600",
  "Vitamin C Face Serum":
    "https://www.dr-rashel.com.pk/cdn/shop/files/pomelli_photoshoot_image_4_5_0510_1.png?v=1778411001&width=1946",
  "Moisturizing Face Cream":
    "https://www.aromatic89.lt/cache/large/product/6826/b3V9aMvoZaV4HZqTCTeQgmWUAGWmNkLcec0vsLxn.webp",
  "Beard Grooming Kit":
    "https://m.media-amazon.com/images/I/81rCSIuYYSL._AC_SX679_.jpg",
  "Yoga Mat 6mm":
    "https://www.gaiam.com/cdn/shop/products/05-64061_6MM-GAIAM-ESSENTIALS-YOGA-MAT-TEAL_C_600x.jpg?v=1668557105",
  "Adjustable Dumbbell Set":
    "https://fitnessgearonline.co.uk/cdn/shop/files/HD-40KG-17IN1.webp?v=1776783206&width=3840",
  "Stainless Steel Shaker Bottle":
    "https://images.nexusapp.co/assets/ce/fe/ed/101147467.jpg",
  "Wireless Gaming Controller":
    "https://i5.walmartimages.com/asr/dd38e00f-0bc0-4721-bedd-6485ff7b4b1d.9d971b63f27e55714b4c2e85cb87807b.jpeg?odnBg=FFFFFF&odnHeight=612&odnWidth=612",
  "RGB Gaming Headset":
    "https://i5.walmartimages.com/seo/Back-School-Savings-JOGZMZ-Gaming-Headset-Microphone-Laptop-Over-Ear-Headphones-LED-RGB-Light-Noise-Canceling-Mic-7-1-Stereo-Surround-Sound_ea1d7638-19d7-4927-8632-c80cd76b6998.fa702397c05f82f9154dadb37798db07.jpeg",
  "Large Gaming Mouse Pad":
    "https://pczonekw.com/cdn/shop/products/GS005GR_121533.jpg?v=1662649909",
  "Water-Resistant Laptop Backpack":
    "https://www.bigw.com.au/medias/sys_master/images/images/h0f/h7f/140367160016926.jpg",
  "Minimal Leather Wallet":
    "https://shfl.com.ua/cdn/shop/files/01_6f5f39c5-d37d-4272-90e3-3e795a95d2c1.jpg?v=1722255756",
  "Classic Analog Watch":
    "https://www.bijouone.hk/cdn/shop/products/B002_fabf4b86-a0f4-4ab4-9724-a2450b332d8b.jpg",
  "Anker PowerCore Power Bank":
    "https://ambassador.com.ph/Uploads/Products/2018/11/anker-powercore-ii-20000-power-bank_11122018-155247-1.jpg",
  "Mechanical Keyboard RGB":
    "https://m.media-amazon.com/images/I/71JhqDP1oTL._SS520_.jpg"
};

const normalize = (value) =>
  String(value || "")
    .trim()
    .toLowerCase()
    .replace(/\s+/g, " ");

const normalizedProductImages = Object.fromEntries(
  Object.entries(productImages).map(([name, url]) => [normalize(name), url])
);

const placeholderByCategory = {
  "Electronics": "/placeholders/electronics.svg",
  "Books": "/placeholders/books.svg",
  "Clothing": "/placeholders/clothing.svg",
  "Home & Kitchen": "/placeholders/home.svg",
  "Beauty & Personal Care": "/placeholders/beauty.svg",
  "Sports & Fitness": "/placeholders/sports.svg",
  "Gaming": "/placeholders/gaming.svg",
  "Bags & Accessories": "/placeholders/bags.svg"
};

export function getProductImage(product) {
  const name = product?.name || product?.productName;
  const mappedImage = normalizedProductImages[normalize(name)];

  if (mappedImage) {
    return mappedImage;
  }

  if (product?.imageUrl) {
    return product.imageUrl;
  }

  return placeholderByCategory[product?.categoryName] || "/placeholders/default.svg";
}

export function handleProductImageError(event, product) {
  const fallback =
    placeholderByCategory[product?.categoryName] || "/placeholders/default.svg";

  if (event.currentTarget.src.includes(fallback)) {
    return;
  }

  event.currentTarget.onerror = null;
  event.currentTarget.src = fallback;
}
