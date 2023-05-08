import React from 'react'
import Header from '../components/Header';
import Footer from '../components/Footer';
import axios from 'axios';
import { useCookies } from 'react-cookie';


import "../components/checkout.css";

export default function CheckOut(props) {
  const [cookies, setCookie] = useCookies(['user']);
  setCookie('pricearray', [], { path: '/checkout'});
  console.log(cookies.selectedBooks)

  var newaddress=cookies.newaddress
  

  function GetDefaultAddress() {
    newaddress='0'
    setCookie('newaddress',0, { path: '/checkout'});
    GetAddress()
  }
  function GetAddress() {
    axios.post(
      'http://localhost:8080/ecommerce/getaddress?userid='+cookies.userid,
      ).then(response=>{
        var Address=response.data[0].firstName+' '+response.data[0].secondName+',</br>'
            Address+=response.data[0].addressLine1+',</br>'
            Address+=response.data[0].addressLine2+',</br>'
            Address+=response.data[0].district+','
            Address+=response.data[0].province+',</br>'
            Address+=response.data[0].postalCode+',</br>'
            Address+=response.data[0].mobile
           // console.log(Address)
        document.getElementById('address').innerHTML=Address
        //console.log(cookies.newaddress)
        if ((newaddress!=='0')) {
          //console.log('***')
          document.getElementById('address').innerHTML=newaddress
          
         }
        
     
       }
        )
        
  }

 
    
       var productquantities=0
        function ShowSelectedProducts() {
         
         axios.post(
          'http://localhost:8080/ecommerce/cart/books?userid='+cookies.userid,
          ).then(response=>{
            //var cart= response.data
            productquantities=response.data
            setCookie('productquantities', response.data, { path: '/checkout'});
            setCookie('product', '<div class="productsdiv">', { path: '/checkout'});
            //console.log(cookies.cart)
            //console.log(cookies.selectedBooks)
            for (let index = 0; index <cookies.selectedBooks.length; index++) {
              cookies.product+='<div class="product">'
              
              //cookies.product+='CartID : '+cookies.selectedBooks[index]
              for (let i = 0; i < response.data.length; i++) {
                var addproduct=' '    
                if (response.data[i].id===cookies.selectedBooks[index]) {
                  addproduct+='<span id="'+response.data[i].item_id+'"></span>'
                  GetProduct(response.data[i].item_id,i,cookies.selectedBooks[index])
                  //break;
               }
               cookies.product+=addproduct
              }
              cookies.product+='</div>'
           }
          // console.log(cookies.product)
           document.getElementById('products').innerHTML=cookies.product+'</div>'

          }
          )
      }
      var pricearray=[]
      
      function GetProduct(book_id,index,cartid){
     //   console.log(book_id)
        var kk=book_id
        var row=' '
        axios.get(
          'http://localhost:8080/ecommerce/books/details?book_id='+kk,
          
          ).then(response=>{
            
         //   console.log('********')
            //console.log(response.data[0].book_price)
            pricearray.push({id:response.data[0].book_id,price:response.data[0].book_price})
            //console.log(response.data[0].book_price)
            
           // setCookie('pricearray', response.data[0].book_price, { path: '/checkout'});
            //cookies.pricearray[index]=response.data[0].book_price
            row+='<div class="maindiv">'
            row+='<img  class="product_image"  id="'+response.data[0].book_id+'_image" style="width:20%;height:70%;">'
            row+='<div class="productInfo"> <p className={Style["booktitle"]}>'
            row+=response.data[0].book_title+'</p>'
            row+='By '+response.data[0].author+'<br/>'
            row+='<p class="price">US $ '+response.data[0].book_price
            row+='</p></div>'
            row+='<div id="change_units" class="unitsdiv"></br></br></br>'
            row+='&nbsp;&nbsp;&nbsp;&nbsp;<img  id="'+response.data[0].book_id+'_remove" class="minus_button" style="width:25px;height:25px;margin: 0 auto;"  />'
            
            row+='&nbsp;&nbsp;&nbsp;&nbsp;<span id="'+response.data[0].book_id+'_units" class="units">'+productquantities[index].quantity+'</span>&nbsp;&nbsp;'
            row+='<img id="'+response.data[0].book_id+'_add" class="add_button" style="width:40px;height:24px;" onClick={addUnits}/></div>'
            row+='</div>' 
            
           // console.log(row)
            document.getElementById(book_id).innerHTML=row
            document.getElementById(response.data[0].book_id+'_image').src=require("../Images/"+response.data[0].book_title+".jpg")
            document.getElementById(response.data[0].book_id+'_remove').src=require("../Images/-.jpeg")
            document.getElementById(response.data[0].book_id+'_add').src=require("../Images/+.jpeg")
           
               // Adding AddUnits and Deduct units in cart onclick functions
       document.getElementById(response.data[0].book_id+'_add').onclick = function () {
        addUnits(response.data[0].num_of_units,index,response.data[0].book_id)
       }
       document.getElementById(response.data[0].book_id+'_remove').onclick = function () {
        RemoveUnits(index,response.data[0].book_id)
       }
       document.getElementById('changeAddressToDefault').onclick = function () {
        GetDefaultAddress()
       }
       
       ChangeTotal()
      
       
          }
          
          )
        
      }
      
      function addUnits(available_units,index,book_id) {
        if (available_units>productquantities[index].quantity) {
          for (let i = 0; i < productquantities.length; i++) {
            if (productquantities[i].item_id===book_id) {
              productquantities[i].quantity+=1
            }
            
          }
          console.log(productquantities[2].id)
          console.log(productquantities)
         axios.post(
          'http://localhost:8080/ecommerce/books/addtocart?book_id='+book_id+'&units='+1+'&userid='+cookies.userid,
          ).then(response=>{
           // ShowProductsInCart()
           }
          )
          ChangeTotal()
        document.getElementById(book_id+'_units').innerHTML=productquantities[index].quantity
        }
    }
    function RemoveUnits(index,book_id) {
     if (productquantities[index].quantity!==1) {
      for (let i = 0; i < productquantities.length; i++) {
        if (productquantities[i].item_id===book_id) {
          productquantities[i].quantity-=1
        }
        
      }
      // console.log(book_id)
      // console.log(cookies.productquantities[index].quantity)
      // console.log(cookies.userid)
       axios.post(
        'http://localhost:8080/ecommerce/cart/updateunits?item_id='+book_id+'&units='+productquantities[index].quantity+'&userid='+cookies.userid,
        ).then(response=>{
         // ShowProductsInCart()
         }
        )
        ChangeTotal()
       document.getElementById(book_id+'_units').innerHTML=productquantities[index].quantity
     }
    }
    var purchaceitems=[]
function ChangeTotal() {

  purchaceitems=[]
  var Total=0
  for (let index = 0; index < cookies.selectedBooks.length; index++) {
  //  console.log('1')

    for(let i=0;i<productquantities.length;i++){
    //  console.log('*')
      for (let m = 0; m < pricearray.length; m++) {

        if (productquantities[i].id===cookies.selectedBooks[index]) {

    if (productquantities[i].item_id===pricearray[m].id) {
      console.log(index)
 
          Total+=productquantities[i].quantity*pricearray[m].price
          m=pricearray.length
          i=productquantities.length
          break;
        }
        
      
    }
     // break;


     
    }

    var singleObj = {};
    singleObj['units'] = productquantities[index].quantity
    singleObj['book_id'] = productquantities[index].item_id
    purchaceitems.push(singleObj);
    if (i===productquantities.length ) {
      break;
    }

    }
  }
  document.getElementById('Total').innerHTML=Total

}



function PlaceOrder() {
  newaddress=0
  setCookie('newaddress',0, { path: '/checkout'});
  var address=document.getElementById('address').innerHTML
  var userid=cookies.userid
  
  const purchaceDetails = {
    purchaceitems,
   address,
   userid ,
   

  };
  console.log(purchaceDetails)
    axios.post(
      'http://localhost:8080/ecommerce/placeorder',
      purchaceDetails,
      ).then(response=>{
        setCookie('orderid', response.data, { path: '/orderplaced'});
        document.getElementById("Checkoutpage").click();  
      }
      )
    }

    return (
        <div>
          <Header/>
          <div className='ordsum'>
            <div className='orderSummayDiv'>
              <h1>Order Summary</h1>
              <p id='total'>&ensp;&ensp;&ensp;Total $ <span id="Total"></span></p><br></br><br></br>
              <div className='place_order_button'><button className='po_button' onClick={PlaceOrder}><b>Place Order</b></button></div>
            </div>
          </div>
        <div className='AddressDiv'>
          <h3>Address</h3>
          <div className='address'>
          <p id="address"  ></p>
          <script>{GetAddress()}</script>
          
        
          

         
          </div>
         
          <div className='changeAdressDiv'>
          <br/>
            <a href='/changeaddress'>+ Add a new Address</a><br/>
            <span  >&nbsp; <button id="changeAddressToDefault" onClick={GetDefaultAddress}>Use Default Address</button></span>
          </div>
        </div>
       
      
        
          <span id="products" ></span>
          
          
          <script>{ShowSelectedProducts()}</script>

        

    
    
          <br/><br/><br/><br/><br/><br/><br/>
          <br/><br/><br/><br/><br/>
          <br/><br/><br/><br/><br/><br/><br/>
    
          <a href='/orderplaced' id="Checkoutpage" > </a>
          <Footer/>
        </div>
      )
    }
    
  