package com.betacom.jpa;

import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

import com.betacom.jpa.auth.AuthTest;
import com.betacom.jpa.carrello.CarrelloOrdineTest;
import com.betacom.jpa.catalogo.CatalogoTest;
import com.betacom.jpa.coupon.CouponTest;

@Suite
@SelectClasses({
	AuthTest.class,
	CatalogoTest.class,
	CouponTest.class,
	CarrelloOrdineTest.class
})
public class SuiteClass {

}
