db = db.getSiblingDB('product-pricing');

db.prices.insertMany([
  {
    brandId: 1,
    startDate: ISODate("2020-06-14T00:00:00Z"),
    endDate: ISODate("2020-12-31T23:59:59Z"),
    priceList: 1,
    productId: 35455,
    priority: 0,
    price: 35.50,
    currency: "EUR"
  },
  {
    brandId: 1,
    startDate: ISODate("2020-06-14T15:00:00Z"),
    endDate: ISODate("2020-06-14T18:30:00Z"),
    priceList: 2,
    productId: 35455,
    priority: 1,
    price: 25.45,
    currency: "EUR"
  },
  {
    brandId: 1,
    startDate: ISODate("2020-06-15T00:00:00Z"),
    endDate: ISODate("2020-06-15T11:00:00Z"),
    priceList: 3,
    productId: 35455,
    priority: 1,
    price: 30.50,
    currency: "EUR"
  },
  {
    brandId: 1,
    startDate: ISODate("2020-06-15T16:00:00Z"),
    endDate: ISODate("2020-12-31T23:59:59Z"),
    priceList: 4,
    productId: 35455,
    priority: 1,
    price: 38.95,
    currency: "EUR"
  }
]);