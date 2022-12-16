// token存、取、删除
export function getTokenName(){
    return 'satoken'
}
export function getTokenValue(){
     return sessionStorage.getItem('satoken')
}
