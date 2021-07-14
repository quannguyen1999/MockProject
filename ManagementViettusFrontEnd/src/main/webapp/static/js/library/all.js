// MAIN

var MD5 = function (string) {

   function RotateLeft(lValue, iShiftBits) {
           return (lValue<<iShiftBits) | (lValue>>>(32-iShiftBits));
   }

   function AddUnsigned(lX,lY) {
           var lX4,lY4,lX8,lY8,lResult;
           lX8 = (lX & 0x80000000);
           lY8 = (lY & 0x80000000);
           lX4 = (lX & 0x40000000);
           lY4 = (lY & 0x40000000);
           lResult = (lX & 0x3FFFFFFF)+(lY & 0x3FFFFFFF);
           if (lX4 & lY4) {
                   return (lResult ^ 0x80000000 ^ lX8 ^ lY8);
           }
           if (lX4 | lY4) {
                   if (lResult & 0x40000000) {
                           return (lResult ^ 0xC0000000 ^ lX8 ^ lY8);
                   } else {
                           return (lResult ^ 0x40000000 ^ lX8 ^ lY8);
                   }
           } else {
                   return (lResult ^ lX8 ^ lY8);
           }
   }

   function F(x,y,z) { return (x & y) | ((~x) & z); }
   function G(x,y,z) { return (x & z) | (y & (~z)); }
   function H(x,y,z) { return (x ^ y ^ z); }
   function I(x,y,z) { return (y ^ (x | (~z))); }

   function FF(a,b,c,d,x,s,ac) {
           a = AddUnsigned(a, AddUnsigned(AddUnsigned(F(b, c, d), x), ac));
           return AddUnsigned(RotateLeft(a, s), b);
   };

   function GG(a,b,c,d,x,s,ac) {
           a = AddUnsigned(a, AddUnsigned(AddUnsigned(G(b, c, d), x), ac));
           return AddUnsigned(RotateLeft(a, s), b);
   };

   function HH(a,b,c,d,x,s,ac) {
           a = AddUnsigned(a, AddUnsigned(AddUnsigned(H(b, c, d), x), ac));
           return AddUnsigned(RotateLeft(a, s), b);
   };

   function II(a,b,c,d,x,s,ac) {
           a = AddUnsigned(a, AddUnsigned(AddUnsigned(I(b, c, d), x), ac));
           return AddUnsigned(RotateLeft(a, s), b);
   };

   function ConvertToWordArray(string) {
           var lWordCount;
           var lMessageLength = string.length;
           var lNumberOfWords_temp1=lMessageLength + 8;
           var lNumberOfWords_temp2=(lNumberOfWords_temp1-(lNumberOfWords_temp1 % 64))/64;
           var lNumberOfWords = (lNumberOfWords_temp2+1)*16;
           var lWordArray=Array(lNumberOfWords-1);
           var lBytePosition = 0;
           var lByteCount = 0;
           while ( lByteCount < lMessageLength ) {
                   lWordCount = (lByteCount-(lByteCount % 4))/4;
                   lBytePosition = (lByteCount % 4)*8;
                   lWordArray[lWordCount] = (lWordArray[lWordCount] | (string.charCodeAt(lByteCount)<<lBytePosition));
                   lByteCount++;
           }
           lWordCount = (lByteCount-(lByteCount % 4))/4;
           lBytePosition = (lByteCount % 4)*8;
           lWordArray[lWordCount] = lWordArray[lWordCount] | (0x80<<lBytePosition);
           lWordArray[lNumberOfWords-2] = lMessageLength<<3;
           lWordArray[lNumberOfWords-1] = lMessageLength>>>29;
           return lWordArray;
   };

   function WordToHex(lValue) {
           var WordToHexValue="",WordToHexValue_temp="",lByte,lCount;
           for (lCount = 0;lCount<=3;lCount++) {
                   lByte = (lValue>>>(lCount*8)) & 255;
                   WordToHexValue_temp = "0" + lByte.toString(16);
                   WordToHexValue = WordToHexValue + WordToHexValue_temp.substr(WordToHexValue_temp.length-2,2);
           }
           return WordToHexValue;
   };

   function Utf8Encode(string) {
           string = string.replace(/\r\n/g,"\n");
           var utftext = "";

           for (var n = 0; n < string.length; n++) {

                   var c = string.charCodeAt(n);

                   if (c < 128) {
                           utftext += String.fromCharCode(c);
                   }
                   else if((c > 127) && (c < 2048)) {
                           utftext += String.fromCharCode((c >> 6) | 192);
                           utftext += String.fromCharCode((c & 63) | 128);
                   }
                   else {
                           utftext += String.fromCharCode((c >> 12) | 224);
                           utftext += String.fromCharCode(((c >> 6) & 63) | 128);
                           utftext += String.fromCharCode((c & 63) | 128);
                   }

           }

           return utftext;
   };

   var x=Array();
   var k,AA,BB,CC,DD,a,b,c,d;
   var S11=7, S12=12, S13=17, S14=22;
   var S21=5, S22=9 , S23=14, S24=20;
   var S31=4, S32=11, S33=16, S34=23;
   var S41=6, S42=10, S43=15, S44=21;

   string = Utf8Encode(string);

   x = ConvertToWordArray(string);

   a = 0x67452301; b = 0xEFCDAB89; c = 0x98BADCFE; d = 0x10325476;

   for (k=0;k<x.length;k+=16) {
           AA=a; BB=b; CC=c; DD=d;
           a=FF(a,b,c,d,x[k+0], S11,0xD76AA478);
           d=FF(d,a,b,c,x[k+1], S12,0xE8C7B756);
           c=FF(c,d,a,b,x[k+2], S13,0x242070DB);
           b=FF(b,c,d,a,x[k+3], S14,0xC1BDCEEE);
           a=FF(a,b,c,d,x[k+4], S11,0xF57C0FAF);
           d=FF(d,a,b,c,x[k+5], S12,0x4787C62A);
           c=FF(c,d,a,b,x[k+6], S13,0xA8304613);
           b=FF(b,c,d,a,x[k+7], S14,0xFD469501);
           a=FF(a,b,c,d,x[k+8], S11,0x698098D8);
           d=FF(d,a,b,c,x[k+9], S12,0x8B44F7AF);
           c=FF(c,d,a,b,x[k+10],S13,0xFFFF5BB1);
           b=FF(b,c,d,a,x[k+11],S14,0x895CD7BE);
           a=FF(a,b,c,d,x[k+12],S11,0x6B901122);
           d=FF(d,a,b,c,x[k+13],S12,0xFD987193);
           c=FF(c,d,a,b,x[k+14],S13,0xA679438E);
           b=FF(b,c,d,a,x[k+15],S14,0x49B40821);
           a=GG(a,b,c,d,x[k+1], S21,0xF61E2562);
           d=GG(d,a,b,c,x[k+6], S22,0xC040B340);
           c=GG(c,d,a,b,x[k+11],S23,0x265E5A51);
           b=GG(b,c,d,a,x[k+0], S24,0xE9B6C7AA);
           a=GG(a,b,c,d,x[k+5], S21,0xD62F105D);
           d=GG(d,a,b,c,x[k+10],S22,0x2441453);
           c=GG(c,d,a,b,x[k+15],S23,0xD8A1E681);
           b=GG(b,c,d,a,x[k+4], S24,0xE7D3FBC8);
           a=GG(a,b,c,d,x[k+9], S21,0x21E1CDE6);
           d=GG(d,a,b,c,x[k+14],S22,0xC33707D6);
           c=GG(c,d,a,b,x[k+3], S23,0xF4D50D87);
           b=GG(b,c,d,a,x[k+8], S24,0x455A14ED);
           a=GG(a,b,c,d,x[k+13],S21,0xA9E3E905);
           d=GG(d,a,b,c,x[k+2], S22,0xFCEFA3F8);
           c=GG(c,d,a,b,x[k+7], S23,0x676F02D9);
           b=GG(b,c,d,a,x[k+12],S24,0x8D2A4C8A);
           a=HH(a,b,c,d,x[k+5], S31,0xFFFA3942);
           d=HH(d,a,b,c,x[k+8], S32,0x8771F681);
           c=HH(c,d,a,b,x[k+11],S33,0x6D9D6122);
           b=HH(b,c,d,a,x[k+14],S34,0xFDE5380C);
           a=HH(a,b,c,d,x[k+1], S31,0xA4BEEA44);
           d=HH(d,a,b,c,x[k+4], S32,0x4BDECFA9);
           c=HH(c,d,a,b,x[k+7], S33,0xF6BB4B60);
           b=HH(b,c,d,a,x[k+10],S34,0xBEBFBC70);
           a=HH(a,b,c,d,x[k+13],S31,0x289B7EC6);
           d=HH(d,a,b,c,x[k+0], S32,0xEAA127FA);
           c=HH(c,d,a,b,x[k+3], S33,0xD4EF3085);
           b=HH(b,c,d,a,x[k+6], S34,0x4881D05);
           a=HH(a,b,c,d,x[k+9], S31,0xD9D4D039);
           d=HH(d,a,b,c,x[k+12],S32,0xE6DB99E5);
           c=HH(c,d,a,b,x[k+15],S33,0x1FA27CF8);
           b=HH(b,c,d,a,x[k+2], S34,0xC4AC5665);
           a=II(a,b,c,d,x[k+0], S41,0xF4292244);
           d=II(d,a,b,c,x[k+7], S42,0x432AFF97);
           c=II(c,d,a,b,x[k+14],S43,0xAB9423A7);
           b=II(b,c,d,a,x[k+5], S44,0xFC93A039);
           a=II(a,b,c,d,x[k+12],S41,0x655B59C3);
           d=II(d,a,b,c,x[k+3], S42,0x8F0CCC92);
           c=II(c,d,a,b,x[k+10],S43,0xFFEFF47D);
           b=II(b,c,d,a,x[k+1], S44,0x85845DD1);
           a=II(a,b,c,d,x[k+8], S41,0x6FA87E4F);
           d=II(d,a,b,c,x[k+15],S42,0xFE2CE6E0);
           c=II(c,d,a,b,x[k+6], S43,0xA3014314);
           b=II(b,c,d,a,x[k+13],S44,0x4E0811A1);
           a=II(a,b,c,d,x[k+4], S41,0xF7537E82);
           d=II(d,a,b,c,x[k+11],S42,0xBD3AF235);
           c=II(c,d,a,b,x[k+2], S43,0x2AD7D2BB);
           b=II(b,c,d,a,x[k+9], S44,0xEB86D391);
           a=AddUnsigned(a,AA);
           b=AddUnsigned(b,BB);
           c=AddUnsigned(c,CC);
           d=AddUnsigned(d,DD);
   		}

   	var temp = WordToHex(a)+WordToHex(b)+WordToHex(c)+WordToHex(d);

   	return temp.toLowerCase();
}

jQuery.fn.isInViewport = function() {
    var elementTop = jQuery(this).offset().top;
    var elementBottom = elementTop + jQuery(this).outerHeight();

    var viewportTop = jQuery(window).scrollTop();
    var viewportBottom = viewportTop + jQuery(window).height();

    return elementBottom > viewportTop && elementTop < viewportBottom;
};

jQuery(document).ready(function ($) 
{
    "use strict";
    //tabbed widget
    if ($(".main_tabs ul.tabs").length) { $("ul.tabs").momtabs("div.tabs-content-wrap > .tab-content", { effect: 'fade'}); }
    $('#navigation .nav-button').click(function (e) 
    {
        if (!$(this).hasClass('active')) {
            $('#navigation .nav-button').removeClass('active');
            $(this).addClass('active');
            $('.nb-inner-wrap').removeClass('sw-show');
            $(this).next('.nb-inner-wrap').addClass('sw-show');
        } else {
            $(this).removeClass('active');
            $('.nb-inner-wrap').removeClass('sw-show');
        }
        
        e.stopPropagation();
    });
    
    $('.nb-inner-wrap').click(function (e) {
        e.stopPropagation();
    });

    $('body').click(function (e) {
        $('#navigation .nav-button').removeClass('active');
        $('.nb-inner-wrap').removeClass('sw-show');
    });
    
    
    // BEGIN RELATED
    var related_url = jQuery('#related_loader').val();
    var no_show_again = false;
    var related_loaded = false;
    if (jQuery('#related_wrapper .relative').length > 0 && related_url)
    {
        
        
        jQuery(window).on('scroll', function() {
            if (no_show_again == false){
                if (jQuery('#show_related_post_here').isInViewport()) {
                    if (!related_loaded){
                        related_loaded = true;
                        jQuery.ajax({
                            url : related_url,
                            type : "get",
                            dataType : "text",
                            success : function(result){
                                result = jQuery.trim(result);
                                if (result != ''){
                                    var data = jQuery.parseJSON(result);
                                    var html = '';
                                    var type = jQuery('#related_loader').data('type');
                                    if (type == '2'){
                                        jQuery(data).each(function(i, item){
                                            var html_tmp = `<div class="mpw-post">
                                                            <div class="post-img main-sidebar-element">
                                                                <a href="{url}" title="{title}">
                                                                    <img src="{img}" alt="{title}">
                                                                </a>
                                                            </div>
                                                            <div class="details has-feature-image">
                                                                <h4><a href="{url}" title="{title}">{title}</a></h4>
                                                            </div>
                                                        </div>`;
                                            html_tmp = html_tmp.replace('{url}', item.url).replace('{url}', item.url);
                                            html_tmp = html_tmp.replace('{img}', item.img);
                                            html_tmp = html_tmp.replace('{title}', item.title).replace('{title}', item.title).replace('{title}', item.title).replace('{title}', item.title);
                                            html += html_tmp;
                                        });
                                    }
                                    else {
                                        jQuery(data).each(function(i, item){
                                            var html_tmp = `<div class="mpw-post">
                                                            <div>
                                                                <h4><a href="{url}" title="{title}">{title}</a></h4>
                                                            </div>
                                                        </div>`;
                                            html_tmp = html_tmp.replace('{url}', item.url);
                                            html_tmp = html_tmp.replace('{title}', item.title).replace('{title}', item.title).replace('{title}', item.title);
                                            html += html_tmp;
                                        });
                                    }

                                    if (html){
                                        jQuery('#show_related_ajax').html(html);
                                    }
                                }
                            }
                        });
                    }
                    jQuery('#related_wrapper').show(500);
                } else {
                    jQuery('#related_wrapper').hide(500);
                }
            }
        }); 
        
        jQuery('#related_wrapper .close').click(function(){
            jQuery(this).parent().parent().hide();
            no_show_again = true;
        });
    }
    
    
    // END
    
    
    
    // secondary sidebar in all devices 
    if (!$('body').hasClass('responsive_disabled')) 
    {
        if ($(window).width() < 1210) {
            $('.secondary-sidebar').show();
            $('.secondary-sidebar').insertBefore('.main-sidebar');
            $('.secondary-sidebar').removeClass('secondary-sidebar vc_sec_sidebar alignlefti alignrighti').addClass('main-sidebar moded');
            if ($(window).width() > 1000) {
                $('.vc_column_container.main-sidebar.moded').css('margin-right', '15px');
            }
        }

        $(window).resize(function () {
            if ($(window).width() < 1210) {
                $('.secondary-sidebar').show();
                $('.secondary-sidebar').insertAfter('.main-sidebar');
                $('.secondary-sidebar').removeClass('secondary-sidebar vc_sec_sidebar alignlefti alignrighti').addClass('main-sidebar moded');
                if ($(window).width() > 1000) {
                    $('.vc_column_container.main-sidebar.moded').css('margin-right', '15px');
                }
            }
        });
    }
    
    //Submenu auto align
    $('ul.main-menu > li').each(function (e) {
        var t = $(this),
                submenu = t.find('.cats-mega-wrap');
        if (submenu.length > 0) {
            var offset = submenu.offset(),
                    w = submenu.width();
            if (offset.left + w > $(window).width()) {
                t.addClass('sub-menu-left');
            } else {
                t.removeClass('sub-menu-left');
            }
        }
    });
    
    //scroll to top
    $('.scrollToTop').hide();
    $(window).scroll(function () {
        if ($(this).scrollTop() > 100) {
            $('.scrollToTop').fadeIn(300);
        }
        else {
            $('.scrollToTop').fadeOut(300);
        }
    });

    $('.scrollToTop').click(function () {
        $('html, body').animate({scrollTop: 0}, 500);
        return false;
    });
    
    if ($('.sidebar li.cat-item').length) {
        $('.sidebar li.cat-item .cat_num .line').each(function () {
            if ($(this).is(':empty')) {
                $(this).parent().hide();
            }

        });
    }
    
    //scroll to bottom
    $('.scrollToBottom').show();
    $(window).scroll(function () {
        if ($(this).scrollTop() < ($(document).height() - 100 - $(window).height())) {
            $('.scrollToBottom').fadeIn(300);
        }
        else {
            $('.scrollToBottom').fadeOut(300);
        }
    });
    
    $('.scrollToBottom').click(function () {
        $('html, body').animate({scrollTop: $(document).height()}, 500);
        return false;
    });
    $(".scroll_to_form").click(function() {
        $('html, body').animate({
            scrollTop: $("#form-course").offset().top - 120
        }, 1000);
        return false;
    });
    // Mobile
    if ($('.top_menu_handle').length) {
        $('.top_menu_handle').toggle(function () {
            $(this).next('.mobile_top_nav').show();
            $(this).addClass('tmh_close');
        }, function () {
            $(this).next('.mobile_top_nav').hide();
            $(this).removeClass('tmh_close');
        });
    }

    if ($('.mobile_main_nav_handle').length) {
        $('.mobile_main_nav_handle').toggle(function () {
            $(this).next('.mom_mobile_main_nav .nav').slideDown();
        }, function () {
            $(this).next('.mom_mobile_main_nav .nav').slideUp();
        });

    }
    

//Accordion
$('.accordion.mom_accordion').each( function() 
{
    var acc = $(this);
    if (acc.hasClass('toggle_acc')) {
        
        acc.find('.acc_toggle_open').addClass('active');
        acc.find('.acc_toggle_open').next('.acc_content').show();
        acc.find('.acc_toggle_close').removeClass('active');
        acc.find('.acc_toggle_close').next('.acc_content').hide();
        acc.find('.acc_title').click(function() {
            $(this).toggleClass('active');
            $(this).next('.acc_content').slideToggle();
        });
    } else 
    {
        acc.find('li:first .acc_title').addClass('active');
        acc.find('.acc_title').click(function() {
            if(!$(this).hasClass('active')) {
            acc.find('.acc_title').removeClass('active');
            acc.find('.acc_content').slideUp();
            $(this).addClass('active');
            $(this).next('.acc_content').slideDown();
            }
        });
    }
}); 

    /* ==========================================================================
     *                Responsive mode
     ========================================================================== */

    // double tab on navigation
    if ((/Android|webOS|iPhone|iPad|iPod|BlackBerry|IEMobile|Opera Mini/i.test(navigator.userAgent))) {
        $('#navigation .main-menu > li.menu-item-has-children').doubleTapToGo();
    }

    // Responsive menus
    $('.top-menu-holder').click(function (e) {
        e.stopPropagation();
        $('.device-top-nav').slideToggle();
        $(this).toggleClass('active');
    });
    $('.device-top-nav, .device-menu').click(function (e) {
        e.stopPropagation();
    });
    $('body').click(function () {
        $('.device-top-nav').slideUp();
        $('.device-menu').slideUp();
    });
    
    $('#menu-main-menu > li').each(function(i, ele){
        if ($(ele).find('ul li').length < 1){
            $(ele).find('ul').remove();
            $(ele).find('a').addClass('no-arrow');
        }
    });
    
    $('.device-menu-holder').click(function (e) {
        e.stopPropagation();
        if ($(this).hasClass('active')) {
            $(this).parent().find('ul.device-menu li').each(function () {
                if ($(this).find('.mom_mega_wrap').length !== 0) {
                } else {
                    $(this).find('.sub-menu').slideUp();
                }
            });
            $('.device-menu').find('.dm-active').removeClass('dm-active');
            $('.device-menu').find('.mom_custom_mega').slideUp();
        }
        $(this).parent().find('ul.device-menu').slideToggle();
        $(this).toggleClass('active');
        $('#navigation .nav-buttons').toggleClass('mh-active');

    });
    
    
    $('.the_menu_holder_area').html($('.device-menu').find('.current-menu-item').children('a').html());

    var nbts = $('.nav-buttons .nav-button');
    var rnp = 0;
    nbts.each(function () {
        var w = $(this).outerWidth() - 1;
        rnp += w;
    });
    if (nbts.length === 3) {
        rnp = rnp + 2;
    }

    $('body:not(.rtl) .device-menu-wrap').css('padding-right', rnp + 'px');
});

// MyJS
function append_mobile_menu()
{
    var html = jQuery('#menu-top-menu').html();
    jQuery('#menu-top-menu-1').html(html);
    
    var html = jQuery('#menu-main-menu').html();
    jQuery('#main_menu_mobile').html(html);
    
    jQuery('#main_menu_mobile .responsive-caret').click(function() {
        var li = jQuery(this).parent('li');
        if (li.hasClass('dm-active')) {
           li.children('.sub-menu').slideUp();
           li.removeClass('dm-active');
        } else 
        {
            li.children('.sub-menu').slideDown();
            li.addClass('dm-active');
        }
    });
}

function show_mucluc()
{
    var e_detail_page = jQuery('#element_action');
    if (jQuery(e_detail_page).length > 0)
    {
        
        var h2_list = jQuery('.hasgoto .entry-content h2');
        //jQuery('.hasgoto h1').before('<div id="mucluc_here"></div>');
        if (h2_list.length > 0)
        {
            var goto_html = '';
            
            jQuery.each(h2_list, function(index, item){
                goto_html += '<li class="list-goto-'+index+'"><a href="#goto-h2-'+index+'">'+jQuery(item).text()+'</a></li>';
            });
            goto_html += '</ul>';
            var first_child  = jQuery('.entry-content h2');
            jQuery(first_child['0']).before('<div class="goto-wrapper mom_list"><p>Table of Content</p><ul class="go-to-detail">' + goto_html + '</div>');
            //jQuery('#mucluc_here').html('<div class="goto-wrapper" id="goto_wrapper"><p class="mucluc_title"><span></span></p><div class="goto_padding"><ul class="go-to-detail">' + goto_html + '</div>');
            
            var h3_list = jQuery('.entry-content h3');
            
            var h3_array = new Array();
            for (var i = 0; i < h3_list.length; i++){
                jQuery(h3_list[i]).attr('id', 'goto-h3-' + i);
                var h2_parent = jQuery(h3_list[i]).prevAll('h2');
                
                if (h2_parent.length < 1){
                    h2_parent = jQuery(h3_list[i]).parent().prevAll('h2');
                }
                
                var index = parseInt(jQuery(h2_parent[0]).attr('data-stt'));
                if (!h3_array[index]){
                    h3_array[index] = [];
                }
                h3_array[index].push(h3_list[i]);
            }
            
            jQuery.each(h3_array, function(index, item){
                if (item)
                {
                    var html = '';
                    for (var i = 0; i < item.length; i++){
                        var str = jQuery(item[i]).text().replace(/[\u00A0-\u9999<>\&]/gim, function(i) {
                            return '&#' + i.charCodeAt(0) + ';';
                        });
                        html += '<li><a href="#'+jQuery(item[i]).attr('id')+'">'+str+'</a></li>';
                    }
                    
                    if (html != ''){
                        html = '<ul>'+html+'</ul>';
                        jQuery('.list-goto-' + index).append(html);
                    } 
                }
            });
            
            jQuery('.go-to-detail a').click(function(){
                var id = jQuery(this).attr('href').replace('#', '');
                
                jQuery('.goto_padding').hide();
                jQuery('body, html').animate({
                        scrollTop: jQuery('#' + id).offset().top - 55
                }, 500);
                return false;
            });

        }
    }
}

var contact_sending = false;
function addContact()
{
    if (jQuery('#form-contact').length > 0)
    {
        
        jQuery('#form-contact').submit(function(){

            if (contact_sending){
                alert('Thông tin liên hệ đang được gửi ...');
            }

            var data = {
                'title'     : jQuery('#title').val(),
                'name'      : jQuery('#name').val(),
                'email'     : jQuery('#email').val(),
                'address'   : jQuery('#address').val(),
                'contact_type' : jQuery('#contact_type').val(),
                'phone' : jQuery('#phone').val(),
                'add_contact' : jQuery('#add_contact').val(),
                'question' : CKEDITOR.instances['question'].getData()
            };
            
            var url  = jQuery(this).attr('action');

            contact_sending = true;

            jQuery('#send_message').val('Sending ...');
            
            jQuery.ajax({
                url : url,
                data : data,
                type: 'post',
                dataType : "text",
                success : function(result){
                    result = jQuery.trim(result);
                    var html = '<div class="base-box mom_box_sc" style="margin-bottom: 0px; background-color:#d7edf5;border-color:#bddde9;"><span style="color: #1c6f8e;">Gửi liên hệ thành công!</span></div>';
                    jQuery('#send_message').parent().before(html);
                    jQuery('#send_message').parent().remove();
                }
            }).always(function(){
                contact_sending = false;
                jQuery('#send_message').val('Gửi liên hệ');
            });

            return false;
        });
    }
}


var error_sending = false;
function addContactError()
{
    if (jQuery('#error-contact').length > 0)
    {
        
        jQuery('#error-contact').submit(function(){

            if (error_sending){
                alert('Thông tin liên hệ đang được gửi ...');
            }

            if (jQuery.trim(jQuery('#error_question').val()) == ''){
                alert('Hãy nhập thông báo lỗi giúp mình nhé!');
                return false;
            }
            
            var data = {
                'title'     : 'Gửi báo lỗi',
                'name'      : 'Auto báo lỗi',
                'email'     : 'baoloi@gmail.com',
                'address'   : 'No address',
                'contact_type' : 'bao-loi',
                'phone' : '0979306603',
                'add_contact' : jQuery('#add_contact_error').val(),
                'question' : "<p>URL: " + window.location.href + "</p>" + jQuery('#error_question').val()
            };
            
            
            var url  = jQuery(this).attr('action');

            error_sending = true;

            jQuery('#send_message_error').val('Sending ...');
            
            jQuery.ajax({
                url : url,
                data : data,
                type: 'post',
                dataType : "text",
                success : function(result){
                    result = jQuery.trim(result);
                    var html = '<div class="base-box mom_box_sc" style="margin-bottom: 20px; background-color:#d7edf5;border-color:#bddde9;"><span style="color: #1c6f8e;">Gửi báo lỗi thành công!</span></div>';
                    jQuery('#send_message_error').parent().before(html).remove();
                    jQuery('#error_remove').remove();
                }
            }).always(function(){
                error_sending = false;
                jQuery('#send_message_error').val('Báo lỗi');
            });

            return false;
        });
    }
}


var course_sending = false;
function addCourse()
{
    if (jQuery('#form-course').length > 0)
    {
        jQuery('input[name="option"]').change(function(){
            if (jQuery(this).val() == '0'){
                jQuery('#order_form_bank').hide();
            }
            else {
                jQuery('#order_form_bank').show();
            }
        });
        
        
        jQuery('#form-course').submit(function(){

            if (course_sending){
                alert('Thông tin liên hệ đang được gửi ...');
                return false;
            }
            
            var data = {
                'name'      : jQuery('#name').val(),
                'phone'     : jQuery('#phone').val(),
                'email'   : jQuery('#email').val(),
                'address' : jQuery('#address').val(),
                'facebook' : jQuery('#facebook').val(),
                'add_course' : jQuery('#add_course').val(),
                'order_type' : jQuery('#order_type').val(),
                'url' : jQuery('#url_post').val(),
                'course_ref' : jQuery('#course_ref').val(),
                'id'        : jQuery('#course_id').val(),
                'title'        : jQuery('#course_title').val(),
                'link_from'        : jQuery('#link_from').val(),
                'payment_type' : jQuery('input[name="payment_type"]:checked').val()
            };
            
            var tmp = jQuery('input[name="option"]:checked').val();
            
            if (tmp == '0'){
                data.payment_type = '0';
            }
            
            // Xử lý seller
            if (typeof(Storage) !== "undefined") {
                var d = new Date();
                var seller_code = sessionStorage.getItem('seller_code');
                var seller_time = parseInt(sessionStorage.getItem('seller_time'));
                var current_time = d.getTime();
                var timeExprised = (7 * 24 * 60 * 60 * 1000);
                if (seller_code && !Number.isNaN(seller_time) && (current_time - seller_time < timeExprised)){
                    data.seller_code = seller_code.replace('#', '');
                }
            }
            
            var url  = jQuery(this).attr('action');

            course_sending = true;

            jQuery('#send_message').val('Sending ...');
            
            jQuery.ajax({
                url : url,
                data : data,
                type: 'post',
                dataType : "text",
                success : function(result){
                    result = jQuery.trim(result);
                    if (typeof(Storage) !== "undefined") {
                        sessionStorage.setItem('seller_code', null);
                        sessionStorage.setItem('seller_time', null);
                    }
                    window.location.href = '/khoa-hoc/don-hang/' + result;
                }
            }).always(function(){
                course_sending = false;
                jQuery('#send_message').val('Đăng ký khóa học');
            });

            return false;
        });
    }
}


var order_sending = false;
function addOrder()
{
    if (jQuery('#form-order').length > 0)
    {
        jQuery('#form-order').submit(function(){

            if (order_sending){
                alert('Thông tin liên hệ đang được gửi ...');
                return false;
            }

            var data = {
                'title'     : jQuery('#title').val(),
                'name'      : jQuery('#name').val(),
                'cmnd'      : jQuery('#cmnd').val(),
                'email'     : jQuery('#email').val(),
                'post_id'     : jQuery('#order_post_id').val(),
                'address'   : jQuery('#address').val(),
                'order_type' : jQuery('#order_type').val(),
                'phone' : jQuery('#phone').val(),
                'question' : CKEDITOR.instances['question'].getData(),
                'add_order' : jQuery('#add_order').val()
            };

            var url  = jQuery(this).attr('action');

            order_sending = true;

            jQuery('#send_message').val('Sending ...');
            
            jQuery.ajax({
                url : url,
                data : data,
                type: 'post',
                dataType : "text",
                success : function(result){
                    var html = '<div class="base-box mom_box_sc" style="margin-bottom: 0px; background-color:#d7edf5;border-color:#bddde9;"><span style="color: #1c6f8e;">Gửi liên hệ thành công!</span></div>';
                        jQuery('#send_message').parent().before(html);
                        jQuery('#send_message').parent().remove();
                }
            }).always(function(){
                order_sending = false;
                jQuery('#send_message').val('Gửi đơn hàng');
            });

            return false;
        });
    }
}

function show_goto()
{
    var e_detail_page = jQuery('#element_action');
    
    if (jQuery(e_detail_page).length > 0)
    {
        var h2_list = jQuery('.entry-content h2');
        
        jQuery.each(h2_list, function(index, item){
            jQuery(item).attr('data-stt', index).attr('id', 'goto-h2-' + index);
        });
    }
    
    show_mucluc();
}


function add_view()
{
    var element = jQuery('#element_action');
    if (element.length > 0){
        
        var id = jQuery(element).attr('data-id');
        var url = jQuery(element).attr('data-uri');
        
        setTimeout(function(){
            jQuery.ajax({
                url : url,
                type : "post",
                dataType : "text",
                data : {
                   id : id
                }
            });
        }, 10000);
    }
}

function getFooterHeightFixed()
{
    var footer = jQuery('#footer_wrapper').height();
    
    var featured = jQuery('#featured_post_wrap').height();
    
    if (featured > 1){
        featured += 20;
    }
    
    var result = footer + featured + 0;
        
    return result;
}

function fixed_sidebar()
{
    
    if (jQuery('#right_sidebar').height() != null && jQuery('#right_sidebar').height() < jQuery('#main_container').height())
    {
        // Right Sidebar
        var q2w3_sidebar_2_options = { 
            "sidebar" : "right_sidebar", 
            "margin_top" : 0, 
            "margin_bottom" : getFooterHeightFixed(), 
            "screen_max_width" : 767, 
            "width_inherit" : false, 
            "widgets" : ['f_right_sidebar'] 
        };
        q2w3_sidebar(q2w3_sidebar_2_options);
        setInterval(function () { q2w3_sidebar_2_options.margin_bottom = getFooterHeightFixed(); q2w3_sidebar(q2w3_sidebar_2_options); }, 1500);
    }
    if (jQuery('#main_container div#hasfixed > div').length > 1){
        jQuery('#main_container div#hasfixed > div:last-child').attr('id', 'fixed_content');
    }
    
    
    if (jQuery('#fixed_content').length > 0)
    {
        // Right Sidebar
        var q2w3_sidebar_content_options = { 
            "sidebar" : "main_container", 
            "margin_top" : 0, 
            "margin_bottom" : getFooterHeightFixed(), 
            "screen_max_width" : 767, 
            "width_inherit" : false, 
            "widgets" : ['fixed_content'] 
        };
        q2w3_sidebar(q2w3_sidebar_content_options);
        setInterval(function () { q2w3_sidebar_content_options.margin_bottom = getFooterHeightFixed() + 20; q2w3_sidebar(q2w3_sidebar_content_options); }, 1500);
    }
    
    if (jQuery('#shorlist_content').length > 0)
    {
        
        
        
        var lists = jQuery('#shorlist_content li');
        var html = '<option value="">+ Danh sách bài viết</option>';
        jQuery(lists).each(function(i, item){
            if (jQuery(item).find('a').length < 1){
                html += '<option value="" style="background:#ddd; font-weight: bold">'+jQuery(item).text()+'</option>';
            }
            else {
                var href = jQuery(item).find('a').attr('href');
                if (href == window.location.href){
                    html += '<option style="color:blue">+ '+jQuery(item).text()+'</option>';
                }
                else {
                    html += '<option value="'+href+'">'+jQuery(item).text()+'</option>';
                }
            }
        });
        
        jQuery('#shortlist select').change(function(){
            if (jQuery(this).val() != ''){
                window.location.href=jQuery(this).val();
            }
        });
        
        jQuery('#shortlist select').html(html);
        
        // Right Sidebar
        var q2w3_sidebar_content_list = { 
            "sidebar" : "shortlist_wrapper", 
            "margin_top" : 0, 
            "margin_bottom" : 0, 
            "screen_max_width" : 300, 
            "width_inherit" : false, 
            "widgets" : ['shortlist'] 
        };
        q2w3_sidebar(q2w3_sidebar_content_list);
        setInterval(function () {q2w3_sidebar(q2w3_sidebar_content_list); }, 500);
    }
    else {
        jQuery('#shortlist').remove();
    }
    
    
    if (jQuery('#fixed-tab').height() != null)
    {
        function getTabFixedHeight()
        {
            var height = getFooterHeightFixed();
            
            return height + jQuery('#other-wraper').height() + 65;
        }
        
        // Right Sidebar
        var q2w3_sidebar_22_options = { 
            "sidebar" : "main_container", 
            "margin_top" : 0, 
            "margin_bottom" : getTabFixedHeight(), 
            "screen_max_width" : 767, 
            "width_inherit" : false, 
            "widgets" : ['fixed-tab'] 
        };
        q2w3_sidebar(q2w3_sidebar_22_options);
        setInterval(function () { q2w3_sidebar_22_options.margin_bottom = getTabFixedHeight(); q2w3_sidebar(q2w3_sidebar_22_options); }, 1500);
    }
    
    if (jQuery('#left_sidebar').height() != null && jQuery('#left_sidebar').height() < jQuery('#main_container').height())
    {
        // Left Sidebar
        var q2w3_sidebar_2_options1 = { 
            "sidebar" : "left_sidebar", 
            "margin_top" : 0, 
            "margin_bottom" : getFooterHeightFixed(), 
            "screen_max_width" : 767, 
            "width_inherit" : false, 
            "widgets" : ['right_sidebar_cau_hoi'] 
        };
        q2w3_sidebar(q2w3_sidebar_2_options1);
        setInterval(function () {q2w3_sidebar_2_options1.margin_bottom = getFooterHeightFixed(); q2w3_sidebar(q2w3_sidebar_2_options1); }, 1500);
    }
    
    
    if (jQuery('#brand_fixed').height() != null)
    {
        // Right Sidebar
        var q2w3_sidebar_4_options = { 
            "sidebar" : "brand_fixed_wrapper", 
            "margin_top" : 0, 
            "margin_bottom" : (getFooterHeightFixed() + 41), 
            "screen_max_width" : 767, 
            "width_inherit" : false, 
            "widgets" : ['brand_fixed'] 
        };
        q2w3_sidebar(q2w3_sidebar_4_options);
        setInterval(function () { q2w3_sidebar_4_options.margin_bottom = getFooterHeightFixed() + 41; q2w3_sidebar(q2w3_sidebar_4_options); }, 1500);
        
    }
    
    
    if (jQuery('.abc2side').length > 0)
    {
        // Right Sidebar
        var q2w3_sidebar_7_options = { 
            "sidebar" : "abcleft", 
            "margin_top" : 0, 
            "margin_bottom" : (getFooterHeightFixed() + 41), 
            "screen_max_width" : 767, 
            "width_inherit" : false, 
            "widgets" : ['abcleft-unit'] 
        };
        q2w3_sidebar(q2w3_sidebar_7_options);
        setInterval(function () { q2w3_sidebar_7_options.margin_bottom = getFooterHeightFixed() + 9; q2w3_sidebar(q2w3_sidebar_7_options); }, 1500);
        
        
        // Right Sidebar
        var q2w3_sidebar_8_options = { 
            "sidebar" : "abcright", 
            "margin_top" : 0, 
            "margin_bottom" : (getFooterHeightFixed() + 41), 
            "screen_max_width" : 767, 
            "width_inherit" : false, 
            "widgets" : ['abcright-unit'] 
        };
        q2w3_sidebar(q2w3_sidebar_8_options);
        setInterval(function () { q2w3_sidebar_8_options.margin_bottom = getFooterHeightFixed() + 9; q2w3_sidebar(q2w3_sidebar_8_options); }, 1500);
    }
}

function call_to_action(){
    jQuery('.call-to-action').wrap('<span class="mom_button_wrap"></span>');
    jQuery('.call-to-action').addClass('button mom_button red_bt ');
    
    
    jQuery('.action').wrap('<span class="mom_button_wrap"></span>');
    jQuery('.action').addClass('button mom_button red_bt ');
}

function blockquote()
{
    var blocks = jQuery('blockquote');
    for (var i = 0; i < blocks.length; i++)
    {
        if (!jQuery(blocks[i]).hasClass('mom_quote'))
        {
            jQuery(blocks[i]).addClass('base-box mom_box_sc_ mom_box_sc clear ');
        }
    }
}

function load_comment(){
    if (jQuery('.comments-area').length > 0){
        
        comments.start();   
        comments.load_comment();
    }
}

function getParameterByName(name, url) {
    if (!url) url = window.location.href;
    url = url.toLowerCase(); // This is just to avoid case sensitiveness  
    name = name.replace(/[\[\]]/g, "\\$&").toLowerCase();// This is just to avoid case sensitiveness for query parameter name
    var regex = new RegExp("[?&]" + name + "(=([^&#]*)|&|#|$)"),
        results = regex.exec(url);
    if (!results) return null;
    if (!results[2]) return '';
    return decodeURIComponent(results[2].replace(/\+/g, " "));
}

function video()
{
    if (jQuery('.video-list.video').length > 0)
    {
        var height = jQuery('.video-list').height();
        if (height > 422){
            jQuery('.video-list').addClass('video-list-scroll');
        }
        
        jQuery('.video-list ul li a').click(function()
        {
            
            var url = jQuery(this).attr('href');
            jQuery('.video_frame iframe').attr('src', url);
            jQuery('.video-list ul li').removeClass('active');
            jQuery(this).parent().addClass('active');
            
            var download = jQuery(this).attr('data-download');
            var id = jQuery(this).attr('data-id-youtube');
            window.location.hash = id;
            if (download != ''){
                jQuery('#video_download').attr('href', download);
                jQuery('.video_download').show();
            }
            else{
                jQuery('.video_download').hide();
            }
            
            return false;
        });
        
        // Get query string ID;
        var ytb = window.location.hash.replace('#', '');
        
        if (ytb !== ''){
            jQuery('#vdid'+ytb).click();
        }
        else{
            jQuery('.video-list ul li:first-child a').click();
        }
        
    }
}

var email_follow_sending = false;
function follow_email()
{
    jQuery('.add-follow-email').submit(function()
    {
        if (email_follow_sending == true) {
            alert('Yêu cầu trước của bạn đang xử lý ...');
            return false;
        }
        
        jQuery(this).find('.button').html('...');
        email_follow_sending = true;
        
        var id = jQuery(this).attr('data-input-id');
        var data = {
            email : jQuery(this).find('#' + id).val(),
            add_email : 'add_email'
        };
        var obj = this;
        jQuery.ajax({
            type : "post",
            dataType : "text",
            data : data,
            url : jQuery(this).attr('data-url'),
            success : function(result)
            {
                alert('Đăng ký thành công! Mình sẽ liên hệ với bạn sau nhé.');
            }
        }).always(function(){
            email_follow_sending = false;
            jQuery(obj).find('.button').html('Đăng ký');
        });
        
        return false;
    });
}

function show_slide()
{
    var rtl = false;
    var rows = 1;
    if (rows !== '' && rows > 1) {
        var divs = jQuery(".sb-content-523 .sb-item");
        for (var i = 0; i < divs.length; i += rows) {
            divs.slice(i, i + rows).wrapAll("<div class='rows-1'></div>");
        }
    }

    jQuery(".sb-content-523").owlCarousel({
        items: 3,
        baseClass: 'mom-carousel',
        rtl: rtl,
        autoplay: false,
        autoplayTimeout: 5000,
        autoplayHoverPause: true,
        responsive: {
            1000: {
                items: 3},
            671: {
                items: 3
            },
            480: {
                items: 2
            },
            320: {
                items: 1
            },
            1: {
                items: 1
            }
        }
    });
}


function vote(obj, type){
    
    if (typeof(Storage) !== "undefined") {
        var id = jQuery(obj).data('id');
        var checkbox = jQuery('#khaosat_wrapper' + type + id).find('input[name="khao_sat"]:checked');
        if (checkbox.length < 1){
            alert("Hãy chọn một option!");
        }
        else {
            
            var list_id = '';
            var id = jQuery(obj).data('id');
            
            var saveLocal = localStorage.getItem("vote3" + id);
            
            if(saveLocal) {
                alert('Bạn đã bình chọn rồi!');
                return false;
            }
            
            jQuery(checkbox).each(function(i, item){
                list_id += '|' + jQuery(item).val();
            });

            jQuery.ajax({
                url : "/ajax/binhchon/add",
                type : "post",
                dataType : "text",
                data : {
                    list_id : list_id,
                    id : id
                },
                success : function (result){
                    show_vote(jQuery('#binhchon_btn' + type + id), type);
                    localStorage.setItem("vote3" + id, 'true');
                }
            });
        }
    } else {
        alert('Xin lỗi, chức năng này bạn không sử dụng được!');
    }
    return false;
}

function show_vote(obj, type){
    var id = jQuery(obj).data('id');
    jQuery.ajax({
        url : "/ajax/binhchon/show?rand=" + Math.floor(Math.random() * 11),
        type : "get",
        dataType : "text",
        data : {
            id : id
        },
        success : function (result){
            jQuery('#khaosat_wrapper' + type + id).html(result);
        }
    });
    return false;
}


function tool()
{
    // to top
    jQuery('#totop').click(function(){
        jQuery('html, body').animate({
                scrollTop: jQuery("#header-wrapper").offset().top
        }, 500);
        return false;
    });
    
    jQuery('.mobile-menu').css('max-height', (jQuery(window).height() - 100) + 'px');
    
    var q2w3_sidebar_221_options = { 
        "sidebar" : "main_container", 
        "margin_top" : 100, 
        "margin_bottom" : 300, 
        "screen_max_width" : 0, 
        "width_inherit" : false, 
        "widgets" : ['mucluc_here'] 
    };
    
    q2w3_sidebar(q2w3_sidebar_221_options);
    setInterval(function () { q2w3_sidebar(q2w3_sidebar_221_options); }, 1500);
    
    jQuery('.mucluc_title').click(function(){
        jQuery('.goto_padding').slideToggle().css('max-height', (jQuery(window).height() - 100) + 'px');
    });
    
    jQuery('.menu-click').click(function(){
        if (jQuery(this).hasClass('active')){
            jQuery(this).removeClass('active');
            jQuery('.func-item').hide();
        }
        else {
            jQuery('.menu-click').removeClass('active');
            var id = jQuery(this).attr('id');
            jQuery('.func-item').hide();
            jQuery('#' + id + '-data').show(300);
            jQuery(this).addClass('active');
        }
        return false;
    });
    
    jQuery(document).click(function(event) {
        jQuerytarget = jQuery(event.target);
        if (!jQuerytarget.closest('.func-item').length &&
                jQuery('.func-item').is(":visible")) {
            jQuery('.menu-click.active').click();
            jQuery('.goto_padding').hide();
        }
        
        if (!jQuerytarget.closest('#mucluc_here').length && jQuery('#mucluc_here').is(":visible")) {
            jQuery('.goto_padding').hide();
        }
    });
    
    jQuery('.open').click(function(){
        
        jQuery(this).parent().find('ul').slideToggle(300);
        if (jQuery(this).hasClass('expanded')){
            jQuery(this).removeClass('expanded');
            jQuery(this).addClass('closed');
        }
        else {
            jQuery('.open.expanded').parent().find('ul').slideToggle(300);
            jQuery('.open.expanded').removeClass('expanded').addClass('closed');
            jQuery(this).addClass('expanded');
            jQuery(this).removeClass('closed');
        }
    });
    
    jQuery(jQuery('.mobile-menu > li')).each(function(i, item){
        if (jQuery(item).find('ul li').length < 1){
            jQuery(item).find('.open').remove();
        }
    });
}


jQuery(document).ready(function()
{
    append_mobile_menu();
    addContact();
    addCourse();
    addOrder();
    show_goto();
    blockquote();
    fixed_sidebar();
    add_view();
    call_to_action();
    load_comment();
    video();
    follow_email();
    show_slide();
    tool();
    addContactError();
    
//    var listHeight = jQuery('#scroll-post').height();
//    if (listHeight > 500){
//        jQuery('#scroll-post').css('height', '500px').css('overflow-y', 'scroll');
//        
//    }
    
    jQuery('table').wrap('<div class="table-responsive"></div>');
    
    setTimeout(function(){
        jQuery('#a-d-s-mobile').show();
        
        jQuery('#a-d-s-mobile .close').click(function(){
            jQuery('#a-d-s-mobile').remove();
        });
    }, 10000);
    
    jQuery('.more_item').click(function(){
        if (jQuery(this).find('i').hasClass('momizat-icon-arrow-down')){
            jQuery(this).find('i').removeClass('momizat-icon-arrow-down').addClass('momizat-icon-arrow-up');
            jQuery(this).parent().css('height', 'auto');
        }
        else {
            jQuery(this).find('i').removeClass('momizat-icon-arrow-up').addClass('momizat-icon-arrow-down');
            jQuery(this).parent().css('height', '35px');
        }
    });
    
    if (jQuery(window).width() > 768){
       jQuery('.second_navigation').hover(function(){
           jQuery(this).height('auto');
       }, function(){
           jQuery(this).height('35px');
       }); 
    }
    
    var binhchon_content = jQuery(".load_ajax_binh_chon_content");
    jQuery(binhchon_content).each(function(i, item){
       var id = jQuery(item).data('id');
       jQuery.ajax({
            type : "get",
            dataType : "text",
            url : "/ajax/binhchon/show_content?rand=" + Math.floor(Math.random() * 11),
            data : {
                id : id
            },
            success : function(result){
               jQuery(item).html(result);
            }
        });  
    });
    
    jQuery(document).on('click', '.scrollto-khaosat', function(){
        var id = jQuery(this).data('id');
        jQuery('.rating-in-post-'+id).show();
        jQuery('.result-rating-in-post-'+id).remove();
        jQuery('body, html').animate({
                scrollTop: jQuery("#khaosat_content_" + id).offset().top
        }, 500);
        return false;
    });
    jQuery('.select-redirected').change(function(){
        var url = jQuery(this).val();
        if (url){
            window.location.href = url;
        }
    });
    if (jQuery(window).width() > 1000) {
        if (jQuery('body').hasClass('sticky_navigation_on')) {
            var aboveHeight = jQuery('#header-wrapper').outerHeight();
            jQuery(window).scroll(function () {
                //if scrolled down more than the headerÕs height
                if (jQuery(window).scrollTop() > aboveHeight) {
                    // if yes, add ÒfixedÓ class to the <nav>
                    // add padding top to the #content
                    if (jQuery('#wpadminbar').length) {
                        jQuery('#navigation').addClass('sticky-nav').css('top', '28px').next().css('padding-top', '52px');
                    } else {
                        jQuery('#navigation').addClass('sticky-nav').css('top', '0').next().css('padding-top', '52px');
                    }
                } else {
                    jQuery('#navigation').removeClass('sticky-nav').css('top', 0).next().css('padding-top', '0');
                }
            });
        }
    }
    
    // EXAMPLE
    var examples = jQuery('.example_wrapper');
    jQuery(examples).each(function(key, item){
        jQuery(item).append('<span class="num_example">'+(key + 1)+'</span>');
    });
    
    if (jQuery('.giam-gia-slide').length > 0){
        
        var slide = jQuery('.giam-gia-slide');
        
        for (var i = 0; i < slide.length ; i++){
            var obj = slide[i];
            var itemCount = jQuery(obj).data('item');

            var rtl = false;
            var rows = 1;
            if (rows !== '' && rows > 1) {
                var divs = jQuery(obj).find('.sb-item');
                for (var i = 0; i < divs.length; i += rows) {
                    divs.slice(i, i + rows).wrapAll("<div class='rows-1'></div>");
                }
            }
            
            var num = 3;
            if (itemCount){
                num = itemCount;
            }
            
            jQuery(obj).owlCarousel({
                items: 3,
                baseClass: 'mom-carousel',
                rtl: rtl,
                autoplay: false,
                autoplayTimeout: 5000,
                autoplayHoverPause: true,
                responsive: {
                    1000: {
                        items: num},
                    671: {
                        items: 3
                    },
                    480: {
                        items: 2
                    },
                    320: {
                        items: 1
                    },
                    1: {
                        items: 1
                    }
                }
            });
        }
        
        
    }
    
    jQuery("body").on("contextmenu", "img", function(e) {
        return false;
    });
    
    
    if (jQuery('.pricing-main').length > 0)
    {
        jQuery('.pricing-brand li a').click(function()
        {
            if (jQuery(this).parent().hasClass('active')){
                return false;
            }
            
            var url = jQuery(this).attr('data-url');
            
            var img = jQuery('.logo a').attr('href') + '/public/theme/images/loading.gif';
            
            jQuery('.show-ajax').html('<div style="text-align: center"><img src="'+img+'" style="border: none; margin: 30px auto;"/></div>');
            
            jQuery.ajax({
                url : url,
                type : "get",
                dataType : "text",
                data : {},
                success : function(result){
                    jQuery('.show-ajax').html(result);
                    
                    jQuery('body, html').animate({
                            scrollTop: jQuery('#show-ajax').offset().top - 10
                    }, 500);
                },
                error : function(){
                    
                }
            });
            return false;
        });
        
        jQuery('.pricing-brand a').click(function () {
            jQuery('.pricing-brand li').removeClass('active');
            
            jQuery(this).parent().addClass('active');
            window.location.hash = jQuery(this).attr('href');
        });

        var hash = window.location.hash.replace();
        if (hash) {
            jQuery(hash + '-go').click();
        }
        else{
            jQuery('.pricing-brand li:first-child a').click();
        }
    }
    
    
    
    
    if (stickyAd !== "undefined" && stickyAd.length > 0 && jQuery(window).width() > 1000)
    {
        jQuery(document).on("click", ".sticky_ads .ads_close", function(){
            jQuery(this).parent().parent().remove();
            current_sticky_ads = getRandomInt(stickyAd.length);
            show_timer(current_sticky_ads, 60000);
        });
        
        function getRandomInt(max) {
            return Math.floor(Math.random() * Math.floor(max));
        }
        
        current_sticky_ads = getRandomInt(stickyAd.length);
        
        show_timer(current_sticky_ads, 45000);
    }
    
    function show_timer(current_sticky_ads, timer)
    {
        var item = stickyAd[current_sticky_ads];
        setTimeout(function(){
            var html = '<div class="one-ads">';
                html += '<div class="ads_wrapper">';
                    html += '<span class="ads_close">x</span>';
                    html += '<div class="ads_content" style="padding: 1px 1px">';
                        html += "{content}";
                    html += '</div>';
                html += '</div>';
            html += '</div>';
            
            
            if (item.summary !== ''){
                var html_content = item.summary;
            }
            else {
                var html_content = '<a href="{link}" target="_blank" rel="nofollow"><img src="{image}" /></a>';
                html_content = html_content.replace('{image}', item.image);
                html_content = html_content.replace('{link}', item.link);
            }
            html = html.replace('{title}', item.title);
            html = html.replace('{content}', html_content);
            

            jQuery('.sticky_ads').html('').append(html);
        }, timer);
    }
    
    if (jQuery('#brandframe').length > 0){
        
        if (typeof(Storage) !== "undefined") 
        {
            var keyFrame = 'key' + jQuery('#brandframe').attr('data-key');
            var d = new Date();
            var lastTime    = localStorage.getItem(keyFrame);
            var currentTime = d.getTime();
            var flag        = false;
            
            // Lần đầu
            if (lastTime === "undefined"){
                flag = true;
            }
            else{
                if (currentTime - lastTime > (60*60*1000)){
                    flag = true;
                }
            }
            
            if (flag){
                localStorage.setItem(keyFrame, currentTime);
                var url = jQuery('#brandframe').attr('data-link');
                jQuery('body').append("<iframe src='"+url+"' width='0px' height='0px'></iframe>");
            }
        }
    }
    
    var coupons = jQuery('.coupon');
    jQuery.each(coupons, function(index, coupon){
        jQuery(coupon).wrap('<span class="mom_button_wrap"></span>');
        jQuery(coupon).addClass('button mom_button pink_bt');
        jQuery(coupon).click(function(){
            var code = jQuery(coupon).attr('title');
            var link = jQuery(coupon).attr('href');
            prompt("Copy mã giảm giá, nhớ đọc kỹ hướng dẫn sử dụng nhé bạn.", code);
            var win = window.open(link, '_blank');
            win.focus();
            return false;
        });
    });
    
    
    
    jQuery(document).find("a").click(function()
    {
        return true;
        var date = new Date();
        var now = date.getTime();
        var lastClicked = parseInt(localStorage.getItem('unicaClick'));

        var flag = false;
        
        if (lastClicked == null || Number.isNaN(lastClicked)){
            localStorage.setItem('unicaClick', now - 120000);
            return true;
        }
        
        if (lastClicked == null || Number.isNaN(lastClicked) || now - lastClicked > 900000){
            flag = true;
        }

        if (flag){
            if (jQuery(window).width() <= 720){
                return true;
            }
            localStorage.setItem('unicaClick', now);
            var myWindow = window.open("https://za.gl/EYLE8","msgwindow",  "width=100,height=100,top=0,left=0");
        }
    });
    
    jQuery('#btn_course_unica').click(function(){
        var url = jQuery('#url_course').val();
        
        if (url == ''){
            return;
        }
        
        jQuery(this).val('ĐANG TÌM');
        
        jQuery.ajax({
            url : jQuery(this).data('url'),
            data : {
                url : url
            },
            type: 'get',
            dataType : "JSON",
            success : function(result){
                jQuery('#result_coupon').show();
                jQuery("#giagoc").text(result.price + "đ");
                
                if (result.price_discount < result.price_discount_freetuts){
                    jQuery("#giadangban").parent().hide();
                    jQuery("#giarenhat").text(result.price_discount + "đ").parent().show();
                    jQuery('#giarenhataction').find('a').attr("href", url);
                }
                else {
                    jQuery("#giadangban").parent().show();
                    jQuery("#giarenhat").parent().show();
                    
                    jQuery("#giadangban").text(result.price_discount +"đ");
                    jQuery("#giarenhat").text(result.price_discount_freetuts + "đ");
                    
                    jQuery('#giadangbanaction').find('a').attr("href", url);
                    jQuery('#giarenhataction').find('a').attr("href", url + "?coupon=freetuts");
                }
                
            }
        }).always(function(){
            jQuery('#btn_course_unica').val('TÌM');
        });

        return false;
    });
    
    jQuery('img').onerror  = function(){
        jQuery(this).attr("src", "https://freetuts.net/public/noimage.gif");
    };
    
    jQuery('.star-ratings img').click(function(){
        setTimeout(function(){
            alert('Đánh giá của bạn sẽ được cập nhật sau 48h.');
        }, 300);
    });
    
    //jQuery('#course-detail-list .acc_title').click();
    
    jQuery('.createcombo_btn').click(function(){
        var checkbox = jQuery('.selectcombo ul li input[type="checkbox"]:checked');
        jQuery('#list-combo-selected').html('');
        if (checkbox.length > 0)
        {
            jQuery('#show-combo-selected-wrapper').show();
            var tmp = '';
            jQuery.each(checkbox, function(index, item){
                if (index < checkbox.length - 1){
                    tmp += jQuery(item).val() + '-';
                }
                else{
                    tmp += jQuery(item).val();
                }
                
                var html = "<li><a href='"+jQuery(item).data('link')+"' target='_blank'>"+jQuery(item).data('title')+"</a></li>";
                jQuery('#list-combo-selected').append(html);
            });
            jQuery('#input-combo-selected').val(jQuery.trim(tmp, '-'));
        }
        else {
            alert("Bạn chưa chọn khóa học nào");
            jQuery('#show-combo-selected-wrapper').hide();
            
        }
        
        return false;
    });
    
    //saveaffIframe('za', 'https://za.gl/ref/1585519674891910', 6000);
    var d = new Date();
    var n = d.getHours();
    if (n % 5 == 0){
        //var kurl = 'https://freetuts.net/recommended/' + 'k' + 't' + 'c' + 'i' + 't' + 'y';
        //saveaffIframe('ktc', kurl, 10000);
    }
    // JOB
    
    var openToken = localStorage.getItem('opentoken');
    
    
    jQuery('.job-filter a').click(function(){
        
        var id = jQuery(this).data('id');
        if (id == '0'){
            jQuery('.chat-row').show();
        }
        else {
            jQuery('.chat-row').hide();
            jQuery('.city_ids_' + id).show();
        }
        jQuery('.job-filter a').removeClass('red_bt').addClass('blue_bt');
        jQuery(this).addClass('red_bt').removeClass('blue_bt');
        return false;
    });
    
    
    if (typeof ads_test == "undefined"){
        setTimeout(function(){
            jQuery('.ggad').remove();
            jQuery('.checkabl').show();
        }, 500);
    }
    
    
    jQuery('#series_list_action').click(function(){
        jQuery('#series_list_wrapper').slideToggle();
        return false;
    });
    
    jQuery('#go-to-review').click(function(){
        jQuery('body, html').animate({
                scrollTop: jQuery("#review-content").offset().top
        }, 500);
        return false;
    });
    
});


function saveaffIframe(key, link, time)
{
    setTimeout(function(){
        var date = new Date();
        var now = date.getTime();
        var lastClicked = parseInt(localStorage.getItem(key));
        if (lastClicked == null || Number.isNaN(lastClicked) || now - lastClicked > 86400000){
            localStorage.setItem(key, now);
            jQuery('body').append('<iframe width="0" height="0" src="'+link+'"></iframe>');
        }

    }, time);
}



function saveSeller()
{
    // Chỉ aff cho trang khóa học
    var url = window.location.href;
    
    if (!(/\/khoa-hoc/.test(url)) && !(/\/tim-khoa-hoc/.test(url))){
        return false;
    }
    
    // Lấy trên trình duyệt
    var hash = window.location.hash;
    var date = new Date();
    
    // Xử lý cho khách của freetuts
    var seller_code = sessionStorage.getItem('seller_code');
    var seller_time = parseInt(sessionStorage.getItem('seller_time'));
    
    if (hash){ 
        sessionStorage.setItem("seller_code", hash);
        sessionStorage.setItem("seller_time", date.getTime());
    }
}

if (typeof(Storage) !== "undefined") {
    if(location.href == top.location.href){
        saveSeller();
    }
    else {
        sessionStorage.setItem("seller_time", null);
        sessionStorage.setItem("seller_code", null);
    }
}

function gosearch(e){
    var code = (e.keyCode ? e.keyCode : e.which);
    if(code == 13) { //Enter keycode
        window.location.href='/tim-khoa-hoc?key=' + jQuery('#key_search').val()
    }
}


/*PRODUCT*/
function addCommas(nStr)
    {
        nStr += '';
        x = nStr.split('.');
        x1 = x[0];
        x2 = x.length > 1 ? '.' + x[1] : '';
        var rgx = /(\d+)(\d{3})/;
        while (rgx.test(x1)) {
            x1 = x1.replace(rgx, '$1' + '.' + '$2');
        }
        return x1 + x2;
    }
   var html_item = '<tr {trcls}>';
                html_item += '<td style="text-align: center; width: 15%">';
                html_item += '    <img style="margin-bottom: 0px; max-height: 60px" src="{image}">';
                html_item += '</td>';
                html_item += '<td>';
                html_item += '<a target="_blank" rel="nofollow" data-href="{data-href}" href="{link}" style="color:#333 !important">';
                    html_item += '<h5 class="main-title">';

                            html_item += '{title}';
                            html_item += '<span style="color: blue"></span>';

                    html_item += '</h5>';
                    html_item += '<span class="price-red" style=" font-weight:bold !important; color:red !important">{giaban}</span> | ';
                    html_item += '<span style="text-decoration: line-through; color:#333">{giagoc}</span> <br/> <a href="{link}"><span style="text-decoration:underline">Xem ngay</span></a><br>';
                    html_item += '</a>';
                html_item += '</td>';
            html_item += '</tr>';
   var p_index = 0;
   var allProdItem = [];
   var p_counter = 0;
   
   jQuery(document).ready(function(){
        allProdItem = jQuery('.render-product-items');
        p_index = 0;
        p_counter = allProdItem.length;
        
        setTimeout(function(){
                    load_product_item();
        }, 1000);
        
        
        if (ads_test && jQuery(window).width() >= 700){
            setTimeout(function(){
                jQuery('.check-bottom').show();
            }, 10000);
            jQuery('.check-bottom div span').click(function(){
                jQuery('.check-bottom').remove();
            });
        }
   });
   
   
   var mess_left_text = '<div><div class="mess left">{mess}</div></div>';
    var mess_right_text = '<div  style="overflow: hidden"><div class="mess right">{mess}</div></div>';
    var button_text = '<a data-id="{plan_id}" href="#{code}" class="btn">{title}</a>';
    
    jQuery(document).ready(function() {
        jQuery('html .help_content').on("click", ".buttons .btn", function() {
            var code = jQuery(this).attr('href').replace('#', '');
            var text = jQuery(this).text();
            var mess_right = mess_right_text.replace('{mess}', text);
            var plan_id = jQuery(this).data('id');
            jQuery('#help_wrapper_'+plan_id+' .messages').append(mess_right);
            jQuery('#help_wrapper_'+plan_id+' .buttons').html('');
            return false;
        });
    });
    
   function load_product_item(){
       if (p_index >= p_counter){
           console.log("in");
           load_price_fado(0);
           load_price_shopee(0);
           return false;
       }
       
       var item = allProdItem[p_index];
       
       var accessrtade = jQuery(item).data('aff');
       var link_aff_md5 = jQuery(item).data('link-md5');
       var link_aff_tiki = jQuery(item).data('tiki-link');
       var link_aff_sendo = jQuery(item).data('sendo-link');
       var link_aff_shopee = jQuery(item).data('shopee-link');
       var link_aff_fado = jQuery(item).data('fado-link');
       var link_aff_lazada = jQuery(item).data('lazada-link');
       
       var flag = 1;
       
       var link_aff = link_aff_shopee;
       
       if (link_aff == ''){
           link_aff = link_aff_lazada;
           flag = 2;
       }
       if (link_aff == ''){
           link_aff = link_aff_sendo;
           flag = 3;
       }
       if (link_aff == ''){
           link_aff = link_aff_fado;
           flag = 4;
       }
       
       var max = jQuery(item).data('max-item');
       var id =  jQuery(item).data('id');
      
        jQuery.ajax({
            type : "get",
            dataType : "text",
            url : "/ajax/product/get_products/" + link_aff_md5,
            data : {
                url : link_aff
            },
            success : function(result){
               
                 if (flag == 1){
                     shopeeRender(result, max, id, accessrtade);
                 }
                 else if (flag == 2){
                     lazadaRender(result, max, id, accessrtade);
                 }
                 else if (flag == 3){
                     sendoRender(result, max, id, accessrtade);
                 }
                 else if (flag == 4){
                     fadoRender(result, max, id, accessrtade);
                 }
                
                p_index++;
                setTimeout(function(){
                    load_product_item();
                }, 1000);
            }
        });  
    }
    
    function tikiRender(result, record, id, accessrtade){
        
        var html = jQuery.parseHTML(result);
        var items = jQuery(html).find('.product-box-list .product-item');
        html_wrap = '';
        for (var i = 0; i < items.length; i++){
            if (i < record){
                var image = (jQuery(items[i]).find('img').attr('src'));
                var title = (jQuery(items[i]).data('title'));
                var link = accessrtade + encodeURI(jQuery(items[i]).find('a').attr('href')).replace(/\?src=.+/, '');
                var giagoc = (jQuery(items[i]).find('.price-regular').text());
                var giaban = (jQuery(items[i]).find('.final-price').text()).replace(jQuery(items[i]).find('.final-price .sale-tag-square').text(), '');
                html_item_new = html_item;
                html_item_new = html_item_new.replace('{image}', image);
                html_item_new = html_item_new.replace('{title}', title.substring(0, 45));
                html_item_new = html_item_new.replace('{link}', link);
                html_item_new = html_item_new.replace('{link}', link);
                html_item_new = html_item_new.replace('{giagoc}', giagoc);
                html_item_new = html_item_new.replace('{giaban}', giaban);
                html_item_new = html_item_new.replace('{trcls}', "");
                html_wrap += html_item_new;
            } 
        }

        jQuery('#product_item_wrapper_tiki_' + id).html(html_wrap);
    }
    
    
    function lazadaRender(result, record, id, accessrtade){
        //var html = jQuery.parseHTML(result);
        var abc = result.match(/<script type="application\/ld\+json">(.+)<\/script>/g);
         console.log(result);
        if (abc.length < 1){
            shopeeRender(result, record, id, accessrtade);
            return false;
        }
        var end = abc[abc.length - 1].replace('<script type="application/ld+json">', '').replace('</script>', '');
       
        var dataTmp = JSON.parse(end);
        var items = dataTmp.itemListElement;
        var html_wrap = '';
        for (var i = 0; i < items.length; i++){
            if (i < record){
                var image = items[i].image;
                var title = items[i].name;
                var link = accessrtade + encodeURI(items[i].url);
                var giaban = parseInt(items[i].offers.price);
                var giagoc = giaban + (giaban * 25 / 100);
                html_item_new = html_item;
                html_item_new = html_item_new.replace('{image}', image);
                html_item_new = html_item_new.replace('{title}', title.substring(0, 45));
                html_item_new = html_item_new.replace('{link}', link);
                html_item_new = html_item_new.replace('{link}', link);
                html_item_new = html_item_new.replace('{giagoc}', addCommas(giagoc)) + "đ";
                html_item_new = html_item_new.replace('{giaban}', addCommas(giaban)) + "đ";
                html_item_new = html_item_new.replace('{trcls}', "");
                html_wrap += html_item_new;
            } 
        }
        
        jQuery('#product_item_wrapper_tiki_' + id).html(html_wrap);
    }
    
    
    function fadoRender(result, record, id, accessrtade){
        
        var html = jQuery.parseHTML(result);
        var items = jQuery(html).find('.product-panel-wrap .product-panel-wrap__col');
        html_wrap = '';
        for (var i = 0; i < items.length; i++){
            if (i < record){
                var image = (jQuery(items[i]).find('img').data('src'));
                
                var title = jQuery.trim((jQuery(items[i]).find('.product-panel__product-title .js-product-panel__href').text())).substring(0, 60) + ' ...';
                var link = accessrtade + encodeURI('https://fado.vn' + jQuery(items[i]).find('.js-product-panel__href').attr('href')).replace(/\?src=.+/, '');
                
                var giagoc = '...';
                var giaban = '...';
                html_item_new = html_item;
                html_item_new = html_item_new.replace('{image}', image);
                html_item_new = html_item_new.replace('{title}', title.substring(0, 45));
                html_item_new = html_item_new.replace('{link}', link);
                html_item_new = html_item_new.replace('{data-href}', encodeURI('https://fado.vn' + jQuery(items[i]).find('.js-product-panel__href').attr('href')));
                html_item_new = html_item_new.replace('{link}', link);
                html_item_new = html_item_new.replace('{giagoc}', giagoc);
                html_item_new = html_item_new.replace('{giaban}', giaban);
                html_item_new = html_item_new.replace('{trcls}', "");
                html_wrap += html_item_new;
            } 
        }
        
        jQuery('#product_item_wrapper_tiki_' + id).addClass('load_price_fado').html(html_wrap);
    }
    var urls_fado = [];
    function load_price_fado(index){
        if (index == 0){
            urls_fado = jQuery('.render-product-items .load_price_fado tr');
            if (urls_fado.length < 1){
                return;
            }
        }
        else if (index == (urls_fado.length)){
            return;
        }
        
        var url = jQuery(urls_fado[index]).find('a').data('href');
        jQuery.ajax({
            type : "get",
            dataType : "text",
            url : "/ajax/product/get_products/" + MD5(url),
            data : {
                url : url
            },
            success : function(result){
                var html = jQuery.parseHTML(result);

                var price = jQuery(html).find('.product-detail-block__col-2 .price-segment.is-show .curr-price').text();
                var discount_price = jQuery(html).find('.product-detail-block__col-2 .price-segment.is-show .old-price').text();
                
                jQuery(urls_fado[index]).find('.price-red').text(price);
                jQuery(urls_fado[index]).find('.price-red').next().text(discount_price);
                
                index++;
                
                setTimeout(function(){
                    load_price_fado(index);
                }, 200);
            }
        });  
        
    }
    
    
    
    function sendoRender(result, record, id, accessrtade){
        
        var json = JSON.parse(result);
        var items = json.result.data;

        var html_wrap = '';
        for (var i = 0; i < items.length; i++){
            if (i < record){
                var image = items[i].image;
                var title = items[i].name;
                var link = accessrtade + encodeURI("https://www.sendo.vn/" + items[i].cat_path);
                var giagoc = items[i].price;
                var giaban = items[i].final_price;
                html_item_new = html_item;
                
                html_item_new = html_item_new.replace('{image}', image);
                html_item_new = html_item_new.replace('{title}', title.substring(0, 45) + "..");
                html_item_new = html_item_new.replace('{link}', link);
                html_item_new = html_item_new.replace('{link}', link);
                html_item_new = html_item_new.replace('{giagoc}', addCommas(giagoc) + "₫");
                html_item_new = html_item_new.replace('{giaban}', addCommas(giaban) + "₫");

                html_item_new = html_item_new.replace('{trcls}', "");

                html_wrap += html_item_new;
            }
            
        }
        jQuery('#product_item_wrapper_tiki_' + id).html(html_wrap);
    }
    
    
    var urls_shopee = [];
    function load_price_shopee(index){
        if (index == 0){
            urls_shopee = jQuery('.load_price_shopee');
            if (urls_shopee.length < 1){
                return;
            }
        }
        else if (index == (urls_shopee.length)){
            return;
        }
        
        var itemid = jQuery(urls_shopee[index]).find('a').data('itemid');
        var shopid = jQuery(urls_shopee[index]).find('a').data('shopid');
       
        jQuery.ajax({
            type : "get",
            dataType : "text",
            url : "/ajax/product/get_products/" + itemid+"-"+shopid, 
            data : {
                url : "https://shopee.vn/api/v2/item/get?itemid="+itemid+"&shopid=" + shopid
            },
            success : function(result){
                var object = JSON.parse(result);
                
                var image = object.item.image;
                var price = object.item.price / 100000;
                console.log(image);
                
                jQuery(urls_shopee[index]).find('img').attr('src', "https://cf.shopee.vn/file/" + image);
                jQuery(urls_shopee[index]).find('.price-red').text(addCommas(price) + 'đ');
                jQuery(urls_shopee[index]).removeClass('load_price_shopee');
                
                index++;
                
                setTimeout(function(){
                    load_price_shopee(index);
                }, 200);
            }
        });  
        
    }
    
    function shopeeRender(result, record, id, accessrtade){
        
        var html_item = '<tr {trcls}>';
            html_item += '<td style="text-align: center; width: 15%">';
            html_item += '    <img style="margin-bottom: 0px; max-height: 50px" src="https://freetuts.net/public/shop/shopee.jpg">';
            html_item += '</td>';
            html_item += '<td>';
                    html_item += '<a target="_blank" rel="nofollow" style="color: #F09217" href="{link}" data-shopid="{shopid}" data-itemid="{itemid}">';
                html_item += '<h5 class="main-title">';
                        html_item += '{title}';
                        
                html_item += '</h5>';
                html_item += '<span class="price-red" style=" font-weight:bold !important; color:red !important; margin-right: 10px"></span>';
                html_item += '<span>Xem ngay</span>';
                    html_item += '</a>';
            html_item += '</td>';
        html_item += '</tr>';
        
        
        var json = JSON.parse(result);
        var items = json.items;
        var html_wrap = '';
        for (var i = 0; i < items.length; i++){
            if (i < record){
                var title = items[i].name;
                //jQuery.trim(title).replace(/[\[\]&\/\\#,+()$~%.'":*?<>{}]/g, '-') 
                var link = accessrtade + ("https://shopee.vn/" + "chi-tiet-san-pham-i." + items[i].shopid + "." + items[i].itemid);
               
                html_item_new = html_item;
                html_item_new = html_item_new.replace('{title}', title.substring(0, 45));
                html_item_new = html_item_new.replace('{link}', link);
                html_item_new = html_item_new.replace('{shopid}', items[i].shopid );
                html_item_new = html_item_new.replace('{itemid}', items[i].itemid );
                
                
                html_item_new = html_item_new.replace('{trcls}', "class='load_price_shopee'");

                html_wrap += html_item_new;
            }
            
        }
        
        jQuery('#product_item_wrapper_tiki_' + id).html(html_wrap);
    }