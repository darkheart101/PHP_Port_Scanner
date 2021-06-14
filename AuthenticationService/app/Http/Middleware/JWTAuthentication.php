<?php


namespace App\Http\Middleware;

use Closure;
use Tymon\JWTAuth\Exceptions\TokenExpiredException;
use Tymon\JWTAuth\Exceptions\TokenInvalidException;
use Tymon\JWTAuth\Facades\JWTAuth;

class JWTAuthentication
{
    /**
     * Handle an incoming request.
     *
     * @param  \Illuminate\Http\Request  $request
     * @param  \Closure  $next
     * @return mixed
     */
    public function handle($request, Closure $next)
    {
        try {
            $user = JWTAUth::parseToken()->authenticate();
        }catch(\Exception $e){
            if( $e instanceof TokenExpiredException){
                return response()->json(['success'=>false,'token'=>JWTAuth::parseToken()->refresh(),'status'=>'expired'],200);
            }
            if($e instanceof TokenInvalidException){
                return response()->json(['success'=>false,'message'=>'token Invalid'],401);
            }
            return response()->json(['success'=>false,'message'=>'token not found'],401);
        }
        return $next($request);
    }
}
