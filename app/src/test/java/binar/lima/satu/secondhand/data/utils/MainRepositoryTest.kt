package binar.lima.satu.secondhand.data.utils

import binar.lima.satu.secondhand.data.helper.ApiHelper
import binar.lima.satu.secondhand.data.local.room.ProductDao
import binar.lima.satu.secondhand.data.remote.ApiService
import binar.lima.satu.secondhand.model.auth.login.GetLoginResponse
import binar.lima.satu.secondhand.model.auth.login.LoginBody
import binar.lima.satu.secondhand.model.auth.login.PostLoginResponse
import binar.lima.satu.secondhand.model.auth.register.PostRegisterResponse
import binar.lima.satu.secondhand.model.auth.register.RegisterBody
import binar.lima.satu.secondhand.model.buyer.order.PostOrderBody
import binar.lima.satu.secondhand.model.buyer.order.PostOrderResponse
import binar.lima.satu.secondhand.model.buyer.wishlist.GetWishlistResponseItem
import binar.lima.satu.secondhand.model.buyer.wishlist.PostWishlistBody
import binar.lima.satu.secondhand.model.buyer.wishlist.PostWishlistResponse
import binar.lima.satu.secondhand.model.notification.GetNotificationResponseItem
import binar.lima.satu.secondhand.model.product.GetDetailProductResponse
import binar.lima.satu.secondhand.model.product.GetProductResponseItem
import binar.lima.satu.secondhand.model.seller.banner.GetSellerBannerResponseItem
import binar.lima.satu.secondhand.model.seller.order.GetSellerOrderResponseItem
import binar.lima.satu.secondhand.model.seller.order.PatchOrderBody
import binar.lima.satu.secondhand.model.seller.order.PatchSellerOrderResponse
import binar.lima.satu.secondhand.model.seller.order.PutOrderBody
import binar.lima.satu.secondhand.model.seller.product.GetSellerCategoryResponseItem
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito

class MainRepositoryTest {
    private lateinit var service : ApiService
    private lateinit var apiHelper: ApiHelper
    private lateinit var repository: MainRepository
    private lateinit var appDao: ProductDao

    @Before
    fun setUp() {
        service = mockk()
        appDao = Mockito.mock(ProductDao::class.java)
        apiHelper = ApiHelper(service)
        repository = MainRepository(apiHelper, appDao)
    }

    @Test
    fun loginUser() : Unit = runBlocking {
        val response = mockk<PostLoginResponse>()
        val loginBody = LoginBody("i", "1")

        every {
            runBlocking {
                service.loginUser(loginBody)
            }
        } returns response

        repository.loginUser(loginBody)

        verify {
            runBlocking {
                service.loginUser(loginBody)
            }
        }
    }

    @Test
    fun getLoginUser() : Unit = runBlocking {
        val response = mockk<GetLoginResponse>()

        every {
            runBlocking {
                service.getLoginUser(TOKEN)
            }
        } returns response

        repository.getLoginUser(TOKEN)

        verify {
            runBlocking {
                service.getLoginUser(TOKEN)
            }
        }
    }

    @Test
    fun registerUser() : Unit = runBlocking {
        val response = mockk<PostRegisterResponse>()
        val registerBody = RegisterBody("aaa", "bbb", "ccc", "ddd", "eee", 81222, "fff")

        every {
            runBlocking {
                service.registerUser(registerBody)
            }
        } returns response

        repository.registerUser(registerBody)

        verify {
            runBlocking {
                service.registerUser(registerBody)
            }
        }
    }

    @Test
    fun getAllCategory() : Unit = runBlocking{
        runBlocking {
            val response = mockk<List<GetSellerCategoryResponseItem>>()

            every {
                runBlocking {
                    service.getAllCategory()
                }
            } returns response

            repository.getAllCategory()

            verify {
                runBlocking {
                    service.getAllCategory()
                }
            }
        }
    }

    @Test
    fun getDetailCategory() : Unit = runBlocking{
        runBlocking {
            val response = mockk<GetSellerCategoryResponseItem>()

            every {
                runBlocking {
                    service.getDetailCategory(3)
                }
            } returns response

            repository.getDetailCategory(3)

            verify {
                runBlocking {
                    service.getDetailCategory(3)
                }
            }
        }
    }

    @Test
    fun getSellerProduct() : Unit = runBlocking {
        runBlocking {
            val response = mockk<List<GetProductResponseItem>>()

            every {
                runBlocking {
                    service.getSellerProduct(TOKEN)
                }
            } returns response

            repository.getSellerProduct(TOKEN)

            verify {
                runBlocking {
                    service.getSellerProduct(TOKEN)
                }
            }
        }
    }

    @Test
    fun getSellerOrder(): Unit = runBlocking {
        runBlocking {
            val response = mockk<List<GetSellerOrderResponseItem>>()

            every {
                runBlocking {
                    service.getSellerOrder(TOKEN, "pending")
                }
            } returns response

            repository.getSellerOrder(TOKEN, "pending")

            verify {
                runBlocking {
                    service.getSellerOrder(TOKEN, "pending")
                }
            }
        }
    }

    @Test
    fun getDetailSellerOrder() {
    }

    @Test
    fun patchSellerOrder() : Unit = runBlocking {
        runBlocking {
            val response = mockk<PatchSellerOrderResponse>()

            every {
                runBlocking {
                    service.patchSellerOrder(TOKEN, 5, PatchOrderBody("accepted"))
                }
            } returns response

            repository.patchSellerOrder(TOKEN, 5, PatchOrderBody("accepted"))

            verify {
                runBlocking {
                    service.patchSellerOrder(TOKEN, 5, PatchOrderBody("accepted"))
                }
            }
        }
    }

    @Test
    fun getSellerBanner() : Unit = runBlocking {
        runBlocking {
            val response = mockk<List<GetSellerBannerResponseItem>>()

            every {
                runBlocking {
                    service.getSellerBanner()
                }
            } returns response

            repository.getSellerBanner()

            verify {
                runBlocking {
                    service.getSellerBanner()
                }
            }
        }
    }

    @Test
    fun getAllProduct() : Unit = runBlocking {
        runBlocking {
            val response = mockk<List<GetProductResponseItem>>()

            every {
                runBlocking {
                    service.getAllProduct("available", 0, "")
                }
            } returns response

            repository.getAllProduct("available", 0, "")

            verify {
                runBlocking {
                    service.getAllProduct("available", 0, "")
                }
            }
        }
    }

    @Test
    fun getProduct() : Unit = runBlocking {
        runBlocking {
            val response = mockk<GetDetailProductResponse>()

            every {
                runBlocking {
                    service.getProduct(35)
                }
            } returns response

            repository.getProduct(35)

            verify {
                runBlocking {
                    service.getProduct(35)
                }
            }
        }
    }

    @Test
    fun postOrder(): Unit = runBlocking {
        runBlocking {
            val response = mockk<PostOrderResponse>()

            every {
                runBlocking {
                    service.postOrder(TOKEN, PostOrderBody(13, 13000))
                }
            } returns response

            repository.postOrder(TOKEN, PostOrderBody(13, 13000))

            verify {
                runBlocking {
                    service.postOrder(TOKEN, PostOrderBody(13, 13000))
                }
            }
        }
    }

    @Test
    fun getBuyerOrder() : Unit = runBlocking {
        runBlocking {
            val response = mockk<List<GetSellerOrderResponseItem>>()

            every {
                runBlocking {
                    service.getBuyerOrder(TOKEN)
                }
            } returns response

            repository.getBuyerOrder(TOKEN)

            verify {
                runBlocking {
                    service.getBuyerOrder(TOKEN)
                }
            }
        }
    }

    @Test
    fun updateBuyerOrder() : Unit = runBlocking {
        runBlocking {
            val response = mockk<PostOrderResponse>()

            every {
                runBlocking {
                    service.updateBuyerOrder(TOKEN, 35, PutOrderBody(13000))
                }
            } returns response

            repository.updateBuyerOrder(TOKEN, 35, PutOrderBody(13000))

            verify {
                runBlocking {
                    service.updateBuyerOrder(TOKEN, 35, PutOrderBody(13000))
                }
            }
        }
    }

    @Test
    fun postBuyerWishList() : Unit = runBlocking {
        runBlocking {
            val response = mockk<PostWishlistResponse>()

            every {
                runBlocking {
                    service.postBuyerWishList(TOKEN, PostWishlistBody(13))
                }
            } returns response

            repository.postBuyerWishList(TOKEN, PostWishlistBody(13))

            verify {
                runBlocking {
                    service.postBuyerWishList(TOKEN, PostWishlistBody(13))
                }
            }
        }
    }

    @Test
    fun getBuyerWishlist() : Unit = runBlocking {
        runBlocking {
            val response = mockk<List<GetWishlistResponseItem>>()

            every {
                runBlocking {
                    service.getBuyerWishList(TOKEN)
                }
            } returns response

            repository.getBuyerWishlist(TOKEN)

            verify {
                runBlocking {
                    service.getBuyerWishList(TOKEN)
                }
            }
        }
    }

    @Test
    fun deleteBuyerWishList() : Unit = runBlocking {
        runBlocking {

            val response = mockk<GetWishlistResponseItem>()

            every {
                runBlocking {
                    service.deleteBuyerWishList(TOKEN, 35)
                }
            }returns response

            repository.deleteBuyerWishList(TOKEN, 35)

            verify {
                runBlocking {
                    service.deleteBuyerWishList(TOKEN, 35)
                }
            }
        }
    }

    @Test
    fun getNotification() : Unit = runBlocking {
        runBlocking {
            val response = mockk<List<GetNotificationResponseItem>>()

            every {
                runBlocking {
                    service.getNotification(TOKEN)
                }
            } returns response

            repository.getNotification(TOKEN)

            verify {
                runBlocking {
                    service.getNotification(TOKEN)
                }
            }
        }
    }

    @Test
    fun patchNotification() : Unit = runBlocking {
        runBlocking {
            val response = mockk<GetNotificationResponseItem>()

            every {
                runBlocking {
                    service.patchNotification(TOKEN, 13)
                }
            } returns response

            repository.patchNotification(TOKEN, 13)

            verify {
                runBlocking {
                    service.patchNotification(TOKEN, 13)
                }
            }
        }
    }

    @Test
    fun addProduct() {
    }

    @Test
    fun getProductDb() {
    }

    @Test
    fun deleteAllProduct() {
    }

    companion object{
        private const val TOKEN = "isdjuoahdahusdhia"
    }
}