import { CanActivateFn, Router } from '@angular/router';
import { AuthServices } from './auth-services';
import { inject } from '@angular/core';

export const authGuardGuard: CanActivateFn = (route, state) => {
  const authservices = inject(AuthServices);
  const router = inject(Router);

  if (authservices.isLogged()) return true;

  return router.createUrlTree(['/login'], { queryParams: { returnUrl: state.url } });
};
