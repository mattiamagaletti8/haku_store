import { Routes } from '@angular/router';
import { Home } from './componenti/home/home';
import { About } from './componenti/about/about';
import { Notfnd } from './componenti/notfnd/notfnd';
import { Login } from './componenti/login/login';
import { Registrazione } from './componenti/registrazione/registrazione';
import { ProdottoDettaglio } from './componenti/prodotto-dettaglio/prodotto-dettaglio';
import { Carrello } from './componenti/carrello/carrello';
import { Checkout } from './componenti/checkout/checkout';
import { OrdiniList } from './componenti/ordini-list/ordini-list';
import { OrdineDettaglio } from './componenti/ordine-dettaglio/ordine-dettaglio';
import { Profilo } from './componenti/profilo/profilo';
import { AdminLayout } from './componenti/admin/admin-layout/admin-layout';
import { AdminCategorie } from './componenti/admin/admin-categorie/admin-categorie';
import { AdminProdotti } from './componenti/admin/admin-prodotti/admin-prodotti';
import { AdminCoupon } from './componenti/admin/admin-coupon/admin-coupon';
import { AdminOrdini } from './componenti/admin/admin-ordini/admin-ordini';
import { authGuardGuard } from './auth/auth-guard-guard';
import { adminGuardGuard } from './auth/admin-guard-guard';

export const routes: Routes = [

    {path :'', pathMatch:'full', redirectTo:'catalogo'},

    {path:'catalogo', component:Home},

    {path:'prodotto/:id', component:ProdottoDettaglio},
    {path:'about', component:About},
    {path:'login', component:Login},
    {path:'registrati', component:Registrazione},

    {path:'carrello', component:Carrello, canActivate:[authGuardGuard]},
    {path:'checkout', component:Checkout, canActivate:[authGuardGuard]},
    {path:'ordini', component:OrdiniList, canActivate:[authGuardGuard]},
    {path:'ordini/:id', component:OrdineDettaglio, canActivate:[authGuardGuard]},
    {path:'profilo', component:Profilo, canActivate:[authGuardGuard]},

    {path:'admin', component:AdminLayout, canActivate:[adminGuardGuard], children:[
        {path:'', pathMatch:'full', redirectTo:'categorie'},
        {path:'categorie', component:AdminCategorie},
        {path:'prodotti', component:AdminProdotti},
        {path:'coupon', component:AdminCoupon},
        {path:'ordini', component:AdminOrdini},
    ]},

    {path:'404', component:Notfnd},
    {path: '**', redirectTo: '404'}
];
