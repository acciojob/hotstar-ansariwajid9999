package com.driver.services;


import com.driver.EntryDto.SubscriptionEntryDto;
import com.driver.model.Subscription;
import com.driver.model.SubscriptionType;
import com.driver.model.User;
import com.driver.repository.SubscriptionRepository;
import com.driver.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
public class SubscriptionService {

    @Autowired
    SubscriptionRepository subscriptionRepository;

    @Autowired
    UserRepository userRepository;

    public Integer buySubscription(SubscriptionEntryDto subscriptionEntryDto){

        //Save The subscription Object into the Db and return the total Amount that user has to pay
        int amount =0;

        if(subscriptionEntryDto.getSubscriptionType().equals("BASIC")){
            amount = 500+ (200*subscriptionEntryDto.getNoOfScreensRequired());
        }else if(subscriptionEntryDto.getSubscriptionType().equals("PRO")){
            amount = 800+ (250*subscriptionEntryDto.getNoOfScreensRequired());
        }else{
            amount = 1000+ (350*subscriptionEntryDto.getNoOfScreensRequired());
        }

        ///it will Automatically captures current date and time , we dont have give any parameters to it
        Date date = new Date();

        Subscription subscription = new Subscription(subscriptionEntryDto.getSubscriptionType()
                ,subscriptionEntryDto.getNoOfScreensRequired(),date,amount);

        ///setting the foreign key
        User user = userRepository.findById(subscriptionEntryDto.getUserId()).get();

        subscription.setUser(user);

        subscriptionRepository.save(subscription);

        return amount;
    }

    public Integer upgradeSubscription(Integer userId)throws Exception{

        //If you are already at an ElITE subscription : then throw Exception ("Already the best Subscription")
        //In all other cases just try to upgrade the subscription and tell the difference of price that user has to pay
        //update the subscription in the repository
        User user = userRepository.findById(userId).get();

        Subscription subscription = user.getSubscription();

        if(subscription.getSubscriptionType().equals("ELITE")){
            throw new Exception("Already the best Subscription");
        }

        if(subscription.getSubscriptionType().equals("BASIC")){

            subscription.setSubscriptionType(SubscriptionType.PRO);
            int prevAmount = subscription.getTotalAmountPaid();
            Date date = new Date();
            subscription.setStartSubscriptionDate(date);
            int totalAmount = 800+250*subscription.getNoOfScreensSubscribed();
            subscription.setTotalAmountPaid(totalAmount);

            subscriptionRepository.save(subscription);

            return totalAmount-prevAmount;
        }

        else if(subscription.getSubscriptionType().equals("PRO")){

            subscription.setSubscriptionType(SubscriptionType.PRO);
            int prevAmount = subscription.getTotalAmountPaid();
            Date date = new Date();
            subscription.setStartSubscriptionDate(date);
            int totalAmount = 1000+350*subscription.getNoOfScreensSubscribed();
            subscription.setTotalAmountPaid(totalAmount);

            subscriptionRepository.save(subscription);

            return totalAmount-prevAmount;
        }

        return 0;
    }

    public Integer calculateTotalRevenueOfHotstar(){

        //We need to find out total Revenue of hotstar : from all the subscriptions combined
        //Hint is to use findAll function from the SubscriptionDb



        return null;
    }

}